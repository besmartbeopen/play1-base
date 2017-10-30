package common;

import java.io.Closeable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.inject.Singleton;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.Data;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import play.jobs.Job;

/**
 * Job per l'avvio degli altri job di tipo batch.
 *
 * @author marco
 *
 */
@Slf4j
@Singleton
public class BatchWorker {

  /**
   * Questra struttura è trasmessa via rest/json.
   *
   */
  @Data
  public static class BatchStatus {

    /**
     * L'identificatore degli status.
     */
    private final String id;

    /**
     * Numero elementi iniziali.
     */
    private final long total;
    /**
     * data/ora di creazione.
     */
    private final LocalDateTime createdAt;
    /**
     * data/ora di avvio (può essere nulla in caso di avvio ritardato).
     */
    private LocalDateTime startedAt;
    /**
     * elementi mancanti al completamento.
     */
    private long missing;
    /**
     * impostata soltanto quando è completato.
     */
    private LocalDateTime completedAt;
    /**
     * eventuale messaggio di successo/stato/errore.
     */
    private String message;
    /**
     * Il nome di questa elaborazione.
     */
    private final String name;

    BatchStatus(String id, long total, String name) {
      this.id = id;
      this.total = total;
      createdAt = LocalDateTime.now();
      startedAt = null;
      missing = total;
      completedAt = null;
      message = null;
      this.name = name;
    }

    public boolean isStarted() {
      return startedAt != null;
    }

    public boolean isCompleted() {
      return completedAt != null;
    }
  }

  private final Cache<String, BatchStatus> batches = CacheBuilder.newBuilder()
      .expireAfterAccess(3600, TimeUnit.HOURS)
      .build();
  private final Executor oneExecutor = Executors.newSingleThreadExecutor();

  /**
   * @param key
   * @return lo status corrispondente a key.
   */
  public BatchStatus get(String key) {
    return batches.getIfPresent(key);
  }

  public Collection<BatchStatus> getListAll() {
    return batches.asMap().values();
  }

  /**
   * @return genera una nuova chiave.
   */
  private String newKey() {
    while (true) {
      val key = UUID.randomUUID().toString();
      if (batches.getIfPresent(key) == null) {
        return key;
      }
    }
  }

  public interface IWorker extends Closeable {

    /**
     * @return indica il nome di questa elaborazione.
     */
    String getName();

    /**
     * @return fa il lavoro (batch) e restituisce true se ha finito, false
     * altrimenti.
     */
    boolean doWork();
    /**
     * @return il numero di elementi mancanti
     */
    long toComplete();
    /**
     * Per la chiusura di questo lavoro.
     */
    default void close() {}
  }

  public void stop(String key) {
    val batch = batches.getIfPresent(key);
    if (batch != null) {
      batch.setCompletedAt(LocalDateTime.now());
      batch.setMessage("cancel by user");
    }
  }

  /**
   * Avvia un batch dal worker fornito, esclusivo o meno.
   *
   * @param worker
   * @param exclusive
   * @return l'identificativo del batch, utile per recuperarne lo stato.
   */
  public String start(IWorker worker, boolean exclusive) {
    val key = newKey();
    val status = new BatchStatus(key, worker.toComplete(), worker.getName());
    batches.put(key, status);

    val executor = exclusive ? oneExecutor : ForkJoinPool.commonPool();
    CompletableFuture.runAsync(() -> {
      try (final IWorker w = worker) {
        status.setStartedAt(LocalDateTime.now());
        log.info("start batchWorker \"{}\".", key);
        while (!status.isCompleted()) {
          if (new Job<Boolean>() {

            @Override
            public Boolean doJobWithResult() {

              // avvio dello step e restituzione del valore.
              boolean result = w.doWork();

              // aggiornamento degli status:
              status.setMissing(w.toComplete());
              return result;
            }
          }.now().get()) {
            status.setCompletedAt(LocalDateTime.now());
            log.info("batchWorker \"{}\" done.", key);
            break;
          }
        }
      } catch (InterruptedException | ExecutionException e) {

        log.error("error on \"{}\": {}", key, worker, e);
        status.setMissing(worker.toComplete());
        status.setCompletedAt(LocalDateTime.now());
        status.setMessage("Errore: " + e.getLocalizedMessage());
      }
    }, executor);
    return key;
  }

  /**
   * Avvia un batch.
   *
   * @param name il nome comune di questa elaborazione
   * @param batchWorker una funzione che deve restituire true quando
   * l'elaborazione è finita, false altrimenti
   * @param toComplete una funzione restituisce il numero degli elementi mancanti
   * @param exclusive se true vengono eseguiti dopo gli altri di tipo esclusivo.
   * @return la chiave univoca per identificare questo batchworker.
   */
  public String start(String name, Supplier<Boolean> batchWorker,
      Supplier<Long> toComplete, boolean exclusive) {
    return start(new IWorker() {

      @Override
      public String getName() {
        return name;
      }

      @Override
      public boolean doWork() {
        return batchWorker.get();
      }

      @Override
      public long toComplete() {
        return toComplete.get();
      }
    }, exclusive);
  }
}
