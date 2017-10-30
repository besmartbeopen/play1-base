
import $ from 'jquery'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.css'

import Pace from 'pace-progress'
import 'pace-progress/themes/blue/pace-theme-flash.css'

import 'bootstrap-modal/js/bootstrap-modalmanager'
import 'bootstrap-modal/js/bootstrap-modal'
import 'bootstrap-modal/css/bootstrap-modal-bs3patch.css'
import 'bootstrap-modal/css/bootstrap-modal.css'

import 'font-awesome/css/font-awesome.css'

import 'x-editable/bootstrap3-editable/js/bootstrap-editable'
import 'x-editable/bootstrap3-editable/css/bootstrap-editable.css'

import moment from 'moment'
import 'moment/locale/it'

import Modernizr from 'modernizr'

import 'Select2'
import 'Select2/select2_locale_it.js'
import 'Select2/select2.css'
import 'select2-bootstrap-css/select2-bootstrap.css'

import 'eonasdan-bootstrap-datetimepicker'
import 'eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.css'

import 'daterangepicker'
import 'daterangepicker/daterangepicker.css'

import 'fullcalendar'
import 'fullcalendar/dist/locale/it.js'
import 'fullcalendar/dist/fullcalendar.css'

// jqueryui subsets:
import 'jquery-ui/themes/base/core.css'
import 'jquery-ui/themes/base/sortable.css'
import 'jquery-ui/ui/core'
import 'jquery-ui/ui/widgets/sortable'
import 'jquery-ui/ui/widgets/mouse'

import 'timeago'
import 'timeago/locales/jquery.timeago.it.js'

// questo è solo stile:
import 'awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css'

import Sortable from 'sortablejs'

import urlParse from 'url-parse'

import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

import 'leaflet-fullscreen'
import 'leaflet-fullscreen/dist/leaflet.fullscreen.css'

import 'trumbowyg'
import 'trumbowyg/dist/langs/it.min.js'
import 'trumbowyg/dist/ui/trumbowyg.css'

import Clipboard from 'clipboard'

import PNotify from 'pnotify'
import 'pnotify/dist/pnotify.css'
import 'pnotify/dist/pnotify.nonblock'
import 'pnotify/dist/pnotify.nonblock.css'
import 'pnotify/dist/pnotify.mobile'
import 'pnotify/dist/pnotify.mobile.css'
import 'pnotify/dist/pnotify.history'
import 'pnotify/dist/pnotify.history.css'
import 'pnotify/dist/pnotify.buttons'
import 'pnotify/dist/pnotify.buttons.css'

import bootbox from 'bootbox'

import * as d3selection from 'd3-selection'
import stackedArea from 'britecharts/src/charts/stacked-area'
import stackedBar from 'britecharts/src/charts/stacked-bar'
import donut from 'britecharts/src/charts/donut'
import legend from 'britecharts/src/charts/legend'
import tooltip from 'britecharts/src/charts/tooltip'
// import colorSchemas from 'britecharts/src/charts/helpers/colors'
import 'britecharts/dist/css/common/common.css'
import 'britecharts/dist/css/charts/donut.css'
import 'britecharts/dist/css/charts/stacked-area.css'
import 'britecharts/dist/css/charts/stacked-bar.css'
// import d3timeFormatIt from 'd3-time-format/locale/it-IT.json'
import * as d3format from 'd3-format'
import d3localeIt from 'd3-format/locale/it-IT.json'

import Dropzone from 'dropzone'
import 'dropzone/dist/basic.css'
import 'dropzone/dist/dropzone.css'

import Handlebars from 'handlebars'

import 'bootstrap-datepicker/dist/js/bootstrap-datepicker.js'
import 'bootstrap-datepicker/dist/locales/bootstrap-datepicker.it.min.js'
import 'bootstrap-datepicker/dist/css/bootstrap-datepicker3.css'

import 'clockpicker/dist/bootstrap-clockpicker.js'
import 'clockpicker/dist/bootstrap-clockpicker.css'

import 'jquery-simplecolorpicker'
import 'jquery-simplecolorpicker/jquery.simplecolorpicker.css'
import 'jquery-simplecolorpicker/jquery.simplecolorpicker-fontawesome.css'

import inView from 'in-view'

// local imports
import 'feedback_init.js'

import 'vendor/jquery.history.js'
import 'vendor/jquery.pjax.js'

import 'roboto-fontface'
import 'main.css'

moment.locale('it-IT')
d3format.formatDefaultLocale(d3localeIt)

delete L.Icon.Default.prototype._getIconUrl

L.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png')
})

$(function ($) {
  $.fn.mustache = function (params) {
    return $(Handlebars.compile(this.html())(params))
  }

  bootbox.setDefaults({locale: 'it', className: 'bootbox_modal'})

  // inserite in dist via copy-webpack-plugin
  $.trumbowyg.svgPath = '/public/dist/trumbowyg-icons.svg'

  PNotify.prototype.options.styling = 'fontawesome'
  PNotify.prototype.options.mouse_reset = false
  PNotify.prototype.options.history.menu = true
  PNotify.prototype.options.nonblock.nonblock = true
  PNotify.prototype.options.buttons.show_on_nonblock = true

  // fix: https://github.com/ivaynberg/select2/issues/1436
  $.fn.modal.Constructor.prototype.enforceFocus = function () {}

  $.fn.select2.defaults.allowClear = true

  $(document).off('.datepicker.data-api')
  // configurazione delle date
  $.fn.datepicker.defaults.format = 'dd/mm/yyyy'
  $.fn.datepicker.defaults.language = 'it'
  $.fn.datepicker.defaults.autoclose = true
  $.fn.datepicker.defaults.orientation = 'bottom'

  $.fn.datetimepicker.defaults.locale = 'it'
  $.fn.datetimepicker.defaults.icons = {
    time: 'fa fa-clock-o',
    date: 'fa fa-calendar',
    up: 'fa fa-arrow-up',
    down: 'fa fa-arrow-down'
  }
  $.pjax.defaults.timeout = 1000
  $.pjax.defaults.maxCacheLength = 0

  // abilita i tempi futuri
  $.timeago.settings.allowFuture = true

  /*
   * Disattiva l'auto discover per i .dropzone (viene attivato nel seguito)
   */
  Dropzone.autoDiscover = false

  // eslint-disable-next-line no-undef
  const _promotionalPeriods = promotionalPeriods.reduce(function (map, item) {
    map[item.name] = [moment(item.startAt), moment(item.endTo)]
    return map
  }, {})

  // trattamento del copia negli appunti
  inView('[data-clipboard-target]').once('enter', el => {
    // eslint-disable-next-line no-new
    new Clipboard(el)
  })

  /**
   * evita i doppi invii sulle submit delle form.
   */
  $(document.body).on('submit', 'form:not(.js-allow-double-submission,[data-reload])', function (e) {
    var $form = $(this)
    if ($form.data('submitted') === true) {
      e.preventDefault()
    } else {
      $form.data('submitted', true)
      $(':input', $form).prop('readOnly', true)
      $('a', $form).disable(true)
      $('.btn', $form).addClass('disabled')
    }
    return true
  })

  /**
   * popup ajax
   */
  $(document.body).on('click', 'button[data-load-popup]', function (e) {
    var $this = $(this)
    if (!$this.data('content')) {
      $.ajax({
        url: $this.data('load-popup')
      }).done(function (result, status) {
        $this.data('content', result)
      }).fail(function () {
        $this.data('content', 'Non disponibile.')
      }).always(function () {
        $this.popover({
          html: true,
          trigger: 'manual'
        }).popover('show')
      })
    } else {
      $this.popover('toggle')
    }
    e.preventDefault()
    e.stopPropagation()
  })

  /**
   * conferma sulle form.
   */
  $(document.body).on('click', 'input[data-confirm], button[data-confirm]', function (e) {
    var $button = $(this)
    var confirm = $button.data('confirm')
    if (confirm) {
      bootbox.confirm(confirm, function (result) {
        if (result) {
          $button.data('confirm', '').trigger('click')
        }
      })
      e.preventDefault()
      e.stopPropagation()
    }
  })

  /**
   * Rende click-abili le icone aggiunte ai campi data/calendario o analoghi.
   */
  $(document.body).on('click', '.input-group-addon:has(.fa)', function (e) {
    // ipotizza una struttura come da manuale bs3: <div><input><span></div>
    $(this).parent().find(':input:first').focus()
  })

  /**
   * Rende (dis)attivabile un elemento con un checkbox.
   */
  $(document.body).on('change', 'input:checkbox[data-toggle-disabled]', function (e) {
    var $this = $(this)
    var $target = $($this.data('toggle-disabled'))
    $target.prop('disabled', !$target.prop('disabled'))
  })

  /**
   * Rende switch-able una serie di selettori con input di tipo radio. In
   * questo modo è possibile scegliere uno dei radio e attivare/disattivare
   * uno dei selettori indicati. La parte disattivata viene collassata e
   * disattivata. La parte attivata viene resa visibile e attivata.
   *
   * <input name="xyz" type="radio" data-toggle="switch" data-target="#one">
   * <input name="xyz" type="radio" data-toggle="switch" data-target="#two">
   *
   * <div id="one" class="collapse">...</div>
   * <div id="two" class="collapse in">...</div>
   */
  $(document.body).on('change', 'input[type=radio][data-toggle=switch]', function (e) {
    const $this = $(this)
    const name = $this.attr('name')
    // per gli altri radio con lo stesso name
    $('input[type=radio][name="' + name + '"]').each(function () {
      const $that = $(this)
      const $target = $($that.data('target'))
      const value = $that.prop('checked')
      // si gestisce il collassamento
      $target.collapse(value ? 'show' : 'hide')
      // di attivano/disattivano le input sottostanti
      $('input', $target).prop('disabled', !value)
    })
  })

  /**
   * Gestione dei pulsanti "a". Significato degli attributi "data-":
   *  - data-async se presente può indicare il selettore del
   *    contenuto prelevato via ajax:
   *    Esempio: <a href="..." data-async="#dataOne">...
   *    Dalla risposta verrà prelevata la prima corrispondenza del
   *    selettore "data-async" per sostituirlo al corrente valore.
   *  - data-reload se presente indica di rimpiazzare l'elemento
   *    destinatario indicato da data-async, ricaricando l'url corrente,
   *    ed estraendo la risorsa indicata da data-async, dopo che ha avuto
   *    successo l'operazione indicata in href.
   *  - data-method se presente indica il metodo http da utilizzare:
   *    GET, POST, DELETE.
   *  - data-confirm se presente indica se occorre prima un consenso esplicito
   *    via bootbox.
   */
  function triggerClick (e) {
    const $this = $(this)
    var selector = $this.data('async')
    var method = $this.data('method')
    var confirm = $this.data('confirm')
    var parsedUrl = urlParse($this.attr('href'), true)
    var history = false
    var reload = $this.data('reload') !== undefined
    var noTimeout = $this.data('async') !== undefined
    var data = {}
    if (confirm) {
      bootbox.confirm(confirm, function (result) {
        if (result) {
          $this.data('confirm', '').trigger('click_inner')
        }
      })
      e.preventDefault()
      return
    }
    if (selector === undefined) {
      selector = '#wrap'
      history = true
    }
    if (method === undefined) {
      method = 'GET'
    } else {
      // TODO: controllare gli altri valori possibili di method?
      method = method.toUpperCase()
    }
    // decode query params
    if (method === 'POST' && parsedUrl.query != null) {
      data = parsedUrl.query
      parsedUrl.query = null
    }
    if (reload) {
      $(selector).standBy()
      $.extend(data, {reloaded: true})
      $.ajax({
        type: method,
        url: parsedUrl.toString(),
        data: data
      }).success(function (result, status, jqXHR) {
        var contentType = jqXHR.getResponseHeader('content-type') || ''
        if (contentType.indexOf('html') > 0) {
          // meglio prendere le notifiche dalla pagina html:
          $(result).showNotifications()
        }
        // reload current page:
        // see http://stackoverflow.com/questions/5404839/how-can-i-refresh-a-page-with-jquery#answer-7609411
        $.ajax({
          url: ''
        }).done(function (iresult, istatus) {
          $(selector).replaceAndImprove(selector, iresult)
        })
      })
    } else {
      var params = {
        type: method,
        data: data,
        url: parsedUrl.toString(),
        dataType: 'html',
        fragment: selector,
        push: history,
        container: selector,
        scrollTo: false
      }
      if (noTimeout) {
        $.extend(params, {timeout: 0})
      }
      $.pjax(params)
    }
    e.preventDefault()
  }
  // Fa in modo di selezionare la voce su cui si è fatto click, deselezionando
  // le altre:
  $(document).on('click', '.tree-node a[data-async]', function (e) {
    $('.tree-node a[data-async]').removeClass('selected')
    $(this).addClass('selected')
  })

  /**
   * Permette di navigare l'albero tramite degli elementi esterni alla lista
   */
  $(document.body).on('click', 'a[data-collapse-show]', function (e) {
    var $target = $($(this).data('collapseShow'))
    $target.collapse('show')
    $target.parents('.tree-node').collapse('show')
  })

  $(document.body).on('click click_inner', 'a[data-confirm],a[data-method],a[data-async]', triggerClick)
  $(document).pjax('a[href]:not([data-confirm],[data-method],[data-async],[data-no-pjax])', '#wrap', {fragment: '#wrap'})

  Pace.options = { restartOnPushState: false }

  $(document).on('pjax:success', function (e, data, status, xhr, options) {
    if (options.container) {
      $(options.container).improve()
    }
  }).on('pjax:send', function () {
    Pace.restart()
  }).on('pjax:complete', function () {
    Pace.stop()
  }).on('pjax:error', function (e, xhr, textStatus, error, options) {
    if (error !== 'timeout' && error !== 'abort') {
      bootbox.alert('Si è verificato un errore: ' + error)
    }
  })

  $(document.body).on('click', '[data-select-filter]', function (e) {
    var $this = $(this)
    var $target = $($this.data('selectFilter'))
    var type = $this.data('filterType')
    var selector = 'option'
    if (type) {
      selector += '[data-type=\'' + type + '\']'
    }
    $target.val($(selector, $target).map(function (idx, item) {
      return $(item).attr('value')
    })).trigger('change')
  })

  // auto ricarica tutta la pagina sui cambiamenti della form ad esclusione
  // delle checkbox usate per le bulk-action
  $(document.body).on('change', 'form[data-autosubmit] :input:not(input[type=checkbox][data-check-all],input[type=checkbox][data-check])', function (e) {
    const $this = $(this)
    if ($this.data().nosubmit !== undefined) {
      return
    }
    $this.closest('form').submit()
  })

  // Le checkbox con data-check-all" possono selezionare tutte le
  // checkbox della stessa lista. Layout tipico:
  // <ul>
  //  <li><input type="checkbox" data-check-all/> ...</li>
  //  <li><input type="checkbox class="check"/> ...</li>
  // </ul>
  $(document.body).on('change', 'input[type=checkbox][data-check-all]', function (e) {
    $('input[type=checkbox][data-check]').prop('checked', $(this).prop('checked'))
      .trigger('change')
  })

  // Per la gestione delle bulk action, tiene sincronizzate le checkbox
  $(document.body).on('change', 'input[type=checkbox][data-check]', function (e) {
    var total = $('input[type=checkbox][data-check]').length
    var checked = $('input[type=checkbox][data-check]:checked').length
    var $checkAll = $('input[type=checkbox][data-check-all]')
    $checkAll.prop('checked', total === checked)
  })

  // Le <form data-reload="#results" ...> sono utilizate per caricare
  // i risultati, con cambiamenti automatici su filtri o ordinamenti.
  // Nota: utilizzando il data-no-history si salta la modifica dello
  // storico.
  var changeEvent = function (e) {
    var $form = $(this).closest('form')
    var noHistory = $form.data('no-history')
    var selector = $form.data('reload')
    var $target = $(selector)
    $target.standBy()
    var addr = $form.prop('action') + '?' + $form.find(':input').serialize()
    $target.load(addr + ' ' + selector, function (response, status, request) {
      $(this).children(':first').unwrap()
      $('.collapse', $form).removeClass('in')
      if (!noHistory) {
        History.replaceState(null, $('title').text(), addr)
      }
      $(selector).removeClass('reloading')
      $(selector).improve()
    })
  }
  $(document.body).on('change', 'form[data-reload] :input:not([data-autochange],[type=checkbox][data-check-all],[type=checkbox][data-check])', changeEvent)
  $(document.body).on('change_text', 'form[data-reload] :input[data-autochange]', changeEvent)

  // supporto alle caselle testuali, con relativo timeout.
  $(document.body).on('input', 'form[data-reload] input[data-autochange]', function (e) {
    var $this = $(this)
    var autochangeTimeout = $this.data('autochange_timeout')
    if (autochangeTimeout) {
      clearTimeout(autochangeTimeout)
      $this.removeData('autochange_timeout', autochangeTimeout)
    }
    $this.data('autochange_timeout', setTimeout(function () {
      $this.trigger('change_text')
    }, 500))
  })

  /**
   * Con le form così:
   *  <form ... data-update="#results">
   * ... si ricarica la sezione indicata nel data-update dall'url corrente
   * dopo aver effettuato l'azione della form.
   *
   */
  $(document.body).on('submit', 'form[data-update]', function (e) {
    const $form = $(this)
    const target = $form.data('update')

    $.ajax({
      type: $form.attr('method'),
      url: $form.attr('action'),
      data: $form.serialize()
    }).success(function (result, status, jqXHR) {
      var contentType = jqXHR.getResponseHeader('content-type') || ''
      if (contentType.indexOf('html') > 0) {
        // prende le notifiche dalla pagina html:
        $(result).showNotifications()
      }
      $(target).standBy()
      // reload current page:
      // see http://stackoverflow.com/questions/5404839/how-can-i-refresh-a-page-with-jquery#answer-7609411
      $.ajax({
        url: ''
      }).done(function (iresult, istatus) {
        $(target).replaceAndImprove(target, iresult)
      })
    })
    e.preventDefault()
  })

  /**
   * form ajax attivate con l'attributo data-async:
   *   data-async deve contenere il target per le risposte di successo
   *   data-async-error deve contenere il target per gli errori.
   */
  $(document.body).on('submit', 'form[data-async]', function (e) {
    const $form = $(this)
    const target = $form.data('async')
    const errorTarget = $form.data('async-error')
    const $target = $(target)
    // $form.find(':input').prop("readonly", true)
    // var bgcolor = $form.css('background-color')
    // $form.css('backround-color', '#e0e0e0')

    $.ajax({
      type: $form.attr('method'),
      url: $form.attr('action'),
      data: $form.serialize()
    }).done(function (data, status) {
      $target.replaceWith($(target, data))
      $(data).showNotifications()
      $(target).parent().improve()
      // disattiva la modale sopra (se c'è).
      $form.parents('.modal').modal('hide')
    }).fail(function (xhr, status, error) {
      if (xhr.status === 400) {
        var $res = $(errorTarget, xhr.responseText)
        var $etarget = errorTarget ? $(errorTarget) : $form
        $etarget.html($res.html()).parent().improve()
      } else {
        bootbox.alert('Si è verificato un errore: ' + error)
      }// else segnala l'errore in qualche modo.
    })
    e.preventDefault()
  })

  $(document.body).on('click', 'a[data-async-modal]', function (e) {
    var $this = $(this)
    var $modal = $($this.data('asyncModal'))
    var href = $this.attr('href')
    $('body').modalmanager('loading')

    $modal.load(href, '', function () {
      $modal.modal().improve()
    })
    e.preventDefault()
  })

  /**
   * Il toggleOpenClose utilizza gli attributi:
   *  - data-toggle-open: per indicare una classe diversa da fa-chevron-down
   *  - data-toggle-close: per indicare una classe diversa da fa-chevron-up
   * Inoltre viene utilizzato il primo i.fa interno all'elemento a#href che
   * corrisponde all'id di e.target.
   */
  function toggleOpenClose (e) {
    var $target = $(e.target)
    var togglerOpen = $target.attr('data-toggler-open') || 'fa-chevron-down'
    var togglerClose = $target.attr('data-toggler-close') || 'fa-chevron-up'
    var $fa = $('a[href="#' + $target[0].id + '"][data-toggle=\'collapse\'] i.fa').first()
    if (e.type === 'hide') {
      $fa.removeClass(togglerOpen).addClass(togglerClose)
    } else {
      $fa.addClass(togglerOpen).removeClass(togglerClose)
    }
  }
  $(document.body).on('hide.bs.collapse', 'section,div', toggleOpenClose)
  $(document.body).on('show.bs.collapse', 'section,div', toggleOpenClose)
  $(document.body).on('shown.bs.collapse', 'section,div', function (e) {
    $('[data-calendar]', e.target).fullCalendar('render')
  })

  $.fn.extend({
    disable: function (state) {
      return this.each(function () {
        var $this = $(this)
        if ($this.is('input, button')) {
          this.disabled = state
        } else {
          $this.toggleClass('disabled', state)
        }
      })
    },
    showNotifications: function () {
      $('[data-notify]', this).each(function () {
        var $this = $(this)
        var title = $this.data('notify')
        var text = $this.text()
        var type = $this.data('notify-type')
        // eslint-disable-next-line no-new
        new PNotify({
          title: title,
          text: text,
          type: type
        })
      })

      $('[data-modal-notify]', this).each(function () {
        const $this = $(this)
        bootbox.alert({
          title: $this.data('modalNotify'),
          message: $this.text()
        })
      })
    },
    replaceAndImprove: function (selector, data) {
      $(data).showNotifications()
      var $value = $(selector, data)
      this.removeClass('reloading')
      // TODO: reenable href, button...
      if ($value.size() === 0) {
        return this.fadeOut().empty()
      } else {
        this.replaceWith($value)
        $(selector).improve()
        return $(selector)
      }
    },
    standBy: function () {
      // TODO: disable href, button...
      var $spinner = $('<span class="text-primary" style="position:absolute; z-index: 10"><i class="fa fa-spin fa-spinner fa-2x"></i</span>').prependTo(this)
      var offset = $spinner.offset()
      $spinner.offset({top: offset.top + 10, left: offset.left + 10})
      return this.addClass('reloading')
    }
  })

  // Da chiamare subito e dopo gli inserimenti nel DOM, visto che i listener
  // sulle modifiche DOM non sono ancora stabili/supportati.
  // Qui dentro occorre usare la this.find al posto del selettore standard $.
  $.fn.improve = function () {
    this.showNotifications()

    $('[data-colorpicker]', this).simplecolorpicker({theme: 'fontawesome'})

    // evita i problemi con i .dropzone, simulandone il comportamento:
    $('form.upload', this).each(function () {
      const $this = $(this)
      const $template = $('.previewTemplate', this)
      const previewTemplate = $template.html()
      // si rimuove il template dal DOM:
      $template.remove()
      // si attiva il dropzone programmaticamente:
      $this.dropzone({
        paramName: 'photo',
        maxFilesize: 1,
        maxFiles: 1,
        autoProcessQueue: true,
        uploadMultiple: false,
        acceptedFiles: 'image/*',
        previewTemplate: previewTemplate,
        previewsContainer: '#photo-container',
        dictDefaultMessage: '<i class="fa fa-upload"></i> <span class="hide-xs">Carica',
        dictFallbackMessage: 'Non è supportato il caricamento',
        dictFileTooBig: 'L\'immagine ({{filesize}}) è più grande del limite {{maxFilesize}}',
        success: function (file, response) {
          const selector = '#current-photo'
          $(selector).remove()
        }
      })
    })

    /*
     * Struttura: map/layer visualizzata via leafletjs.
     * Esempio:
     *
     * <map data-legend="...json array..." base="osm">
     *  <layer centered ...
     * </map>
     */
    $('map', this).each(function () {
      const $map = $(this)
      var legendData = $map.data('legend') || false
      var basemap = $map.attr('basemap')
      var minZoom = $map.data('minZoom') || 8
      var maxZoom = $map.data('maxZoom') || 15
      var baseurl
      var attribution
      var loading = 0

      switch (basemap) {
        case 'osm':
        case 'openstreetmap':
          baseurl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
          attribution = '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
          break
        default:
          baseurl = 'http://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png'
          attribution = '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> | &copy; <a href="http://carto.com/">Carto</a>'
      }

      const map = L.map(this, { fullscreenControl: true })
      // var drawnItems = L.featureGroup().addTo(map)

      L.tileLayer(baseurl, {
        maxZoom: maxZoom,
        minZoom: minZoom,
        attribution: attribution
      }).addTo(map)

      if (legendData) {
        const legend = L.control({position: 'bottomright'})
        legend.onAdd = function (map) {
          var div = L.DomUtil.create('div', 'info legend')
          var ll = []
          legendData.forEach(function (item) {
            ll.push('<i style="background-color: ' + item.color + '"></i>' + item.label)
          })
          div.innerHTML = ll.join('<br>')
          return div
        }
        legend.addTo(map)
      }

      const loadingControl = L.control({position: 'topright'})
      loadingControl.onAdd = function (map) {
        const div = L.DomUtil.create('div', 'info')
        div.innerHTML = '<i class="fa fa-spinner fa-2x fa-spin"></i>'
        return div
      }
      loadingControl.addTo(map)

      // Would benefit from https://github.com/Leaflet/Leaflet/issues/4461

      function orDefault (value, defaultValue) {
        return isNaN(value) ? defaultValue : value
      }

      $('layer', this).each(function () {
        const $layer = $(this)
        var color = $layer.attr('color')
        var fillOpacity = orDefault(parseFloat($layer.attr('fill-opacity')), 0.5)
        var highlightOpacity = orDefault(parseFloat($layer.attr('highlight-opacity')),
          fillOpacity + 0.2)
        var weight = orDefault(parseFloat($layer.attr('weight')), 1.0)
        var ref = $layer.attr('geojson-ref')
        // selector = $layer.attr('selector')
        var unbound = Boolean($layer.attr('unbound'))
        const popup = L.popup()
        const defaultStyle = {
          color: color,
          weight: weight,
          dashArray: '2',
          fillOpacity: fillOpacity
        }
        var geolayer

        loading++

        $.getJSON(ref, function (data) {
          function highlightFeature (e) {
            const layer = e.target

            layer.setStyle({
              weight: weight + 1,
              dashArray: '',
              fillOpacity: highlightOpacity
            })

            if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
              layer.bringToFront()
            }
          }

          function resetHighlight (e) {
            e.target.setStyle(defaultStyle)
          }

          function style (feature) {
            if (feature.geometry.type === 'Point') {
              return
            }
            return $.extend({}, defaultStyle, {
              fillColor: feature.properties.fillColor
            })
          }

          function onEachFeature (feature, layer) {
            if (feature.geometry.type === 'Point') {
              return
            }
            layer.on({
              mouseover: highlightFeature,
              mouseout: resetHighlight,
              click: function (e) {
                if (e.target.feature.properties.popup) {
                  popup.setLatLng(e.latlng)
                    .setContent(e.target.feature.properties.popup)
                    .openOn(map)
                }
              }
            })
          }

          geolayer = L.geoJSON(data, {
            style: style,
            onEachFeature: onEachFeature
          }).bindTooltip(function (layer) {
            return layer.feature.properties.name
          }).addTo(map)
          // addNonGroupLayers(geolayer, drawnItems)

          if (!unbound && geolayer.getBounds().isValid()) {
            map.fitBounds(geolayer.getBounds())
              .setMaxBounds(geolayer.getBounds().pad(0.5))
          }

          loading--
          if (loading === 0) {
            loadingControl.remove(map)
          }
        })
      })
      $(this).on('click', 'map a[data-method]', function () {
        map.invalidateSize()
      })
    })

    /**
     * Supporto all'ordinabilità degli elenchi.
     *
     * In data-sortable ci deve essere l'indirizzo da utilizzare
     * per salvare l'ordinamento.
     */
    $('.list-group[data-sortable]', this).each(function () {
      var updateUrl = $(this).data('sortable')
      var sortable = Sortable.create(this, {
        handle: '.fa-ellipsis-v',
        onUpdate: function (evt) {
          $.post(updateUrl, {
            ordered: sortable.toArray()
          })
          // TODO: aggiungere feedback visivo all'utente?
        }
      })
    })

    /**
     * Implementazione di progressbar autoaggiornanti su timer (1 secondo).
     * Caratteristiche ed uso:
     *
     * - la data di avvio indica se è partito e fa cambiare di colore
     * - la data di completamento indica se è finito e fa cambiare il colore
     * - il messaggio nel dato restituito implica un errore (quindi danger).
     * - la percentuale di completamento è determinata dai valori:
     *  (data.total - data.missing) * 100 / data.total
     * - il nome è indicato in data.name
     *
     */
    $('div.progress[data-progressbar]', this).each(function () {
      const $this = $(this)
      var addr = $this.data('progressbar')
      // si ricarica ogni secondo
      var id = setInterval(progress, 1000)
      function progress () {
        const $progressbar = $('.progress-bar', $this)
        const $title = $('span', $this)
        if ($progressbar.length) {
          $.ajax({
            url: addr
          }).done(function (data, status) {
            var percent = '' + (data.total === 0 ? 100
              : ((data.total - data.missing) * 100 / data.total)).toFixed(1) + '%'
            $progressbar.width(percent)
            $title.text(data.name + ' ' + percent)
            // è finito?
            if (data.completedAt) {
              if (data.message) {
                $progressbar.addClass('progress-bar-danger')
              } else {
                $progressbar.addClass('progress-bar-success')
              }
              $progressbar.removeClass('active')
              clearInterval(id)
            } else if (data.startedAt) {
              // è in avviato
              $progressbar.addClass('active')
            } else {
              // no, allora si mette 100% in attesa
              $progressbar.removeClass('active')
            }
          }).fail(function (jqXHR, textStatus, errorThrown) {
            clearInterval(id)
            $progressbar.removeClass('active').addClass('progress-bar-warning')
          })
        } else {
          // non esiste più
          clearInterval(id)
        }
      }
    })

    $('textarea.wysiwyg', this).trumbowyg({
      lang: 'it',
      autogrow: true,
      btns: [
        ['formatting'],
        'btnGrp-semantic',
        'btnGrp-justify',
        'btnGrp-lists',
        ['horizontalRule'],
        ['removeformat'],
        ['fullscreen']
      ]
    })

    $('input[data-clockpicker]', this).each(function () {
      const $this = $(this)
      $this.clockpicker({
        donetext: 'ok',
        autoclose: true,
        afterDone: function () {
          $this.trigger('change')
        }
      })
    })

    $('input[type=date], input[data-type=date], input[data-provide=datepicker]',
      this).each(function () {
      const $this = $(this)
      $this.data().nosubmit = true
      $this.datepicker().on('show', function (e) {
        delete $this.data().nosubmit
      })
    })

    $('input[data-provide=datetimepicker]', this).datetimepicker()

    // È possibile caricare il contenuto di un collapsable solo dopo la
    // sua apertura.
    // Esempio: div data-collapse-async="@UrlDaCaricare..."
    $('[data-collapse-async]', this).each(function () {
      const $this = $(this)
      $this.on('show.bs.collapse', function () {
        $this.load($this.data('collapseAsync'), function () {
          $this.improve()
        })
      })
    })

    // caricamento asincrono di segmenti html
    // WARNING: non utilizzare in cascata... può essere ricorsivo.
    $('[data-load-async]', this).each(function () {
      const $this = $(this)
      $this.load($this.data('loadAsync'), function () {
        $this.improve()
      })
    })

    // x-editable automatici:
    $('a[data-pk]', this).each(function () {
      const $this = $(this)
      var data = {}
      if ($this.data('type') === 'select2') {
        data = {select2: {width: '200', placeholder: '(Scegli un valore...)'}}
      }
      $this.editable(data)
    })

    var select2base = {dropdownAutoWidth: true}
    if (Modernizr.touchevents) {
      select2base['minimumResultsForSearch'] = -1
    }
    // e' possibile chiamare la data-selectize con ulteriori parametri del
    // tipo data-qualcosa.
    $('select[data-selectize]', this).select2(select2base)

    $('input[data-selectize-multiple]', this).each(function () {
      const $this = $(this)
      $this.select2($.extend({}, select2base, {
        multiple: true,
        data: $this.data('selectizeMultiple')
      }))
    })

    $('input[data-options]', this).each(function () {
      const $this = $(this)
      const options = $this.data('options')
      const data = $.map(options, function (item) {
        return { id: item, text: item }
      })
      $this.select2($.extend({}, select2base, {
        createSearchChoice: function (term) {
          return { id: term, text: term }
        },
        data: data
      }))
    })

    $(':input[data-selectize-url]', this).each(function () {
      const $this = $(this)
      const surl = $this.data('selectize-url')
      $this.select2({
        minimumInputLength: 1,
        allowClear: true,
        initSelection: function (element, callbackf) {
          callbackf({ id: element.val(), text: element.data('valueLabel') })
        },
        ajax: {
          url: surl,
          dataType: 'json',
          data: function (term, page) {
            return {
              q: term,
              page: page
            }
          },
          results: function (data, page) {
            return {
              results: $.map(data, function (val, i) {
                return {
                  id: val.value,
                  text: val.label
                }
              })
            }
          }
        }
      })
    })
    $('input[data-datetime-range]', this).each(function () {
      $(this).daterangepicker({
        timePicker: true,
        timePicker12Hour: false,
        timePickerIncrement: 60,
        startDate: $(this).data('datetime-start'),
        endDate: $(this).data('datetime-end'),
        minDate: $(this).data('datetime-min'),
        maxDate: $(this).data('datetime-max'),
        locale: {
          format: 'DD/MM/YYYY HH:mm',
          customRangeLabel: 'Seleziona intervallo',
          fromLabel: 'da',
          toLabel: 'a',
          applyLabel: 'Ok',
          cancelLabel: 'Annulla'
        }
      })
    })
    $('input[data-date-range-small]', this).daterangepicker({
      locale: {
        format: 'DD/MM/YYYY',
        customRangeLabel: 'Seleziona intervallo',
        fromLabel: 'da',
        toLabel: 'a',
        applyLabel: 'Ok',
        cancelLabel: 'Annulla'
      }
    })
    $('input[data-date-range]', this).daterangepicker({
      ranges: $.extend({}, _promotionalPeriods, {
        'Il mese scorso': [ moment().subtract(1, 'month').startOf('month'),
          moment().subtract(1, 'month').endOf('month')]
      }),
      startDate: moment().subtract(29, 'days'),
      endDate: moment(),
      autoUpdateInput: false,
      locale: {
        format: 'DD/MM/YYYY',
        customRangeLabel: 'Seleziona intervallo',
        fromLabel: 'da',
        toLabel: 'a',
        applyLabel: 'Ok',
        cancelLabel: 'Annulla'
      }
    })

    $('input[data-date-range],input[data-date-range-small]', this).on('cancel.daterangepicker', function (ev, picker) {
      $(this).val('')
    }).on('apply.daterangepicker', function (ev, picker) {
      $(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' +
          picker.endDate.format('DD/MM/YYYY'))
      $(this).trigger('change')
    })

    // calendario generico, utilizzato per ora sulle distanze.
    $('[data-calendar-from]', this).each(function () {
      const $this = $(this)
      // target della modale:
      const $modal = $($this.data('calendarModal'))
      $this.fullCalendar({
        defaultDate: $this.data('calendarDate'),
        eventSources: [{
          url: $this.data('calendarFrom'),
          type: 'GET',
          error: function () {
            bootbox.alert('there was an error while fetching data!')
          }
        }],
        eventClick: function (event, jsEvent, view) {
          if (event.url === undefined) {
            return false
          }

          $('body').modalmanager('loading')

          // senza parametri, visto che è una get.
          $modal.load(event.url, '', function (response, status, xhr) {
            if (status === 'error') {
              $('body').modalmanager('loading') // toggle loading
              bootbox.alert('Errore')
            } else {
              $modal.modal().improve()
            }
          })
          // don't follow event.url
          return false
        },
        dayClick: function (date, jsEvent, view) {
          // composizione dell'url con la data
          const parsed = urlParse($this.data('calendarClick'), true)
          parsed.query.date = date.format()
          $('body').modalmanager('loading')

          // senza parametri, visto che è una get.
          $modal.load(parsed.toString(), '', function () {
            $modal.modal().improve()
          })
        }
      })
    })

    function getFullCalendarFromSource () {
      const $this = $(this)
      // const operatorId = $this.data('operatorId')
      const modal = $this.data('calendarModal')
      const $modal = $(modal)
      var data = {
        eventLimit: {
          agenda: 6,
          'default': true
        },
        loading: function (loading) {
          if (loading) {
            $this.addClass('reloading')
            var $spinner = $('<span class="text-primary" style="position:absolute; z-index: 10"><i class="fa fa-spin fa-spinner fa-2x"></i</span>')
            $this.before($spinner)
            var offset = $spinner.offset()
            $spinner.offset({
              top: offset.top + 10,
              left: offset.left + 10
            })
          } else {
            $this.removeClass('reloading')
            $this.prev('span.text-primary').remove()
          }
        },
        eventOrder: 'id',
        eventClick: function (event, jsEvent, view) {
          if (event.url === undefined) {
            return false
          }
          if (!modal) {
            return true
          }

          $('body').modalmanager('loading')

          // senza parametri, visto che è una get.
          $modal.load(event.url, '', function (response, status, xhr) {
            if (status === 'error') {
              $('body').modalmanager('loading') // toggle loading
              bootbox.alert('Errore')
            } else {
              $modal.modal().improve()
            }
          })
          // don't follow event.url
          return false
        },
        eventSources: []
      }
      if ($this.data('calendarDate')) {
        data['defaultDate'] = $this.data('calendarDate')
      }
      if ($this.data('calendarDisableHeader')) {
        data['header'] = false
      }
      data['eventSources'].push({
        url: $this.data('calendarSource'),
        type: 'GET',
        error: function () {
          bootbox.alert('there was an error while fetching data!')
        }
      })
      if ($this.data('calendarClick')) {
        data['dayClick'] = function (date, jsEvent, view) {
          // composizione dell'url con la data
          const parsed = urlParse($this.data('calendarClick'), true)
          parsed.query.date = date.format()
          $('body').modalmanager('loading')

          // senza parametri, visto che è una get.
          $modal.load(parsed.toString(), '', function () {
            $modal.modal().improve()
          })
        }
      }
      $this.fullCalendar(data)
    }

    inView('[data-calendar-source]').on('enter', el => {
      getFullCalendarFromSource.apply(el)
    })

    $('[data-calendar-week]', this).each(function () {
      const $this = $(this)
      const data = {
        defaultView: 'agendaWeek',
        header: false,
        columnFormat: 'dddd',
        minTime: '07:00:00',
        maxTime: '21:00:00',
        eventOverlap: false,
        slotEventOverlap: false,
        allDaySlot: false,
        // slotDuration: '00:15:00',
        snapDuration: '00:15:00',
        // slotLabelInterval: '00:30:00',
        slotLabelFormat: 'H:mm',
        contentHeight: 'auto',
        eventLimit: 6,
        loading: function (loading) {
          if (loading) {
            $this.addClass('reloading')
            var $spinner = $(
              '<span class="text-primary" style="position:absolute; z-index: 10"><i class="fa fa-spin fa-spinner fa-2x"></i</span>'
            )
            $this.before($spinner)
            var offset = $spinner.offset()
            $spinner.offset({
              top: offset.top + 10,
              left: offset.left + 10
            })
          } else {
            $this.removeClass('reloading')
            $this.prev('span.text-primary').remove()
          }
        },
        eventClick: function (event, jsEvent, view) {
          if (event.url === undefined) {
            return false
          }
          window.open(event.url, '_self')
        },
        eventSources: []
      }

      data['eventSources'].push({
        url: $this.data('calendarWeek'),
        type: 'GET',
        error: function () {
          bootbox.alert('there was an error while fetching data!')
        }
      })
      if ($this.data('calendarClick')) {
        data['dayClick'] = function (date, jsEvent, view) {
          // $(this) si riferisce al <td> associato a date
          // composizione dell'url con la data
          const parsed = urlParse($this.data('calendarClick'), true)
          parsed.query.dateTime = date.format()
          window.open(parsed.toString(), '_self')
        }
      }
      if ($this.data('calendarDrop')) {
        data['eventStartEditable'] = true
        data['eventDrop'] = function (event, delta, revertFunc) {
          var url = $this.data('calendarDrop')
          $.ajax({
            type: 'POST',
            url: url,
            data: {
              id: event.id,
              startAt: event.start.format(),
              endTo: event.end.format()
            },
            error: revertFunc,
            success: function () {
              // bootbox.alert('successfully modified')
              // Si comunica in qualche modo il corretto salvataggio?
            }
          })
        }
      }
      if ($this.data('calendarResize')) {
        data['eventDurationEditable'] = true
        data['eventResize'] = function (event, delta, revertFunc) {
          var url = $this.data('calendarResize')
          $.ajax({
            type: 'POST',
            url: url,
            data: {
              id: event.id,
              startAt: event.start.format(),
              endTo: event.end.format()
            },
            error: revertFunc,
            success: function () {
              // bootbox.alert('successfully modified')
              // Si comunica in qualche modo il corretto salvataggio?
            }
          })
        }
      }

      $this.fullCalendar(data)
    })

    // select [multi] -> sortable
    $('select[data-sortable]', this).each(function () {
      const select = $(this)
      $(select).select2()

      const ul = $(select).prev('.select2-container').first('ul')
      ul.sortable({
        placeholder: 'ui-state-highlight',
        items: 'li:not(.select2-search-field)',
        tolerance: 'pointer',
        stop: function () {
          $($(ul).find('.select2-search-choice').get().reverse()).each(function () {
            var id = $(this).data('select2Data').id
            var option = select.find('option[value="' + id + '"]')[0]
            $(select).prepend(option)
          })
        }
      })
    })

    // + used in VisitDoctors & TherapeuticAreas
    $('input[data-sortable-options]', this).each(function () {
      const $this = $(this)
      const options = $this.data('sortableOptions')
      $this.select2($.extend({}, select2base, {
        multiple: true,
        allowClear: true,
        data: options
      }))
      $this.select2('container').find('ul.select2-choices').sortable({
        containment: 'parent',
        start: function () { $this.select2('onSortStart') },
        update: function () { $this.select2('onSortEnd') }
      })
    })
    // ---

    $('input[data-toggle="popover"], button[data-toggle="popover"]', this)
      .popover()
    $('span[data-toggle="tooltip"]', this).tooltip()
    $('[data-toggle="popover-hover"]').popover({
      trigger: 'hover',
      placement: 'right auto',
      container: 'body'
    })

    function getLegendChart (containerSelector, dataset, colorSchema, legendFormat) {
      if (!containerSelector) {
        // senza container non si crea la legenda.
        return
      }
      const legendContainer = d3selection.select(containerSelector)
      const containerWidth = legendContainer.node()
        ? legendContainer.node().getBoundingClientRect().width : false
      const legendChart = legend()
      if (!containerWidth) {
        // se non c'è spazio on si crea
        return
      }
      // si toglie la legenda se ci fosse già
      legendContainer.selectAll('.britechart-legend').remove()
      legendChart.width(containerWidth * 0.8)
        .height(200)
        .numberFormat(legendFormat === undefined ? 's' : legendFormat)
      if (colorSchema) {
        legendChart.colorSchema(colorSchema)
      }
      legendContainer.datum(dataset).call(legendChart)
      return legendChart
    }

    /**
     * Grafici a torta/ciambella il data-donutchart deve essere un elenco di
     * oggetti che devono avere id, name, value, percentage e color.
     *
     * La funzione preleva il selettore dal contesto (this).
     *
     * Da notare che in data-donutchart ci va direttamente il json.
     *
     * Se presente l'attributo data-legend-container mostra una legenda
     * correlata nel box selezionato dal contenuto stesso del selettore
     * data-legend-container.
     */
    function getDonutChart () {
      const $this = $(this)
      const donutContainer = d3selection.select(this)
      const containerWidth = donutContainer.node()
        ? donutContainer.node().getBoundingClientRect().width : false
      const donutChart = donut()
      const dataset = $this.data('donutchart')
      const colorSchemaData = dataset.map(a => a.color).filter(a => a !== undefined && a)
      const colorSchema = colorSchemaData.length
        ? colorSchemaData : undefined
      const containerSelector = $this.data('legend-container')
      const legendFormat = $this.data('legend-format')
      const legendChart = getLegendChart(containerSelector, dataset,
        colorSchema, legendFormat)

      /*
       * Si elimina il precedente grafico, per rendere la funzione idempotente.
       */
      donutContainer.selectAll('.donut-chart').remove()

      if (!containerWidth) {
        return
      }
      donutChart
        .isAnimated(true)
        .width(containerWidth)
        .height(containerWidth / 1.8)
        .externalRadius(containerWidth / 5)
        .internalRadius(containerWidth / 10)
        .on('customMouseOver', function (data) {
          if (legendChart) {
            legendChart.highlight(data.data.id)
          }
        })
        .on('customMouseOut', function () {
          if (legendChart) {
            legendChart.clearHighlight()
          }
        })

      if (colorSchema) {
        donutChart.colorSchema(colorSchema)
      }
      donutContainer.datum(dataset).call(donutChart)
    }

    // si computa soltanto quando visibile:
    inView('[data-donutchart]').on('enter', el => {
      getDonutChart.apply(el)
    })

    /**
     * Indica un grafico a barre con la struttura dati fornita in data-barchart
     * questo elenco di oggetti deve essere composto da
       */
    $('[data-stackedbarchart]', this).each(function (i) {
      const $this = $(this)
      const thisSelector = this
      const container = d3selection.select(this)
      const containerWidth = container.node()
        ? container.node().getBoundingClientRect().width : false
      const chart = stackedBar()
      const datasetUrl = $this.data('stackedbarchart')
      const chartTooltip = tooltip()

      chart.margin({
        left: 110,
        top: 10,
        right: 10,
        bottom: 30
      })
        .tooltipThreshold(600)
        .width(containerWidth)
        .grid('vertical')
        .isHorizontal(true)
        .on('customMouseOver', chartTooltip.show)
        .on('customMouseMove', chartTooltip.update)
        .on('customMouseOut', chartTooltip.hide)

      $.getJSON(datasetUrl, function (dataset) {
        if (container.datum() === undefined) {
          if (dataset.colors) {
            chart.colorSchema(dataset.colors)
          }
          container.datum(dataset.data).call(chart)

          // inizializzazione
          chartTooltip.topicLabel('values')
            .dateLabel('name')
            .nameLabel('stack')
            .title('Informazioni:')

          d3selection.select(thisSelector)
            .select('.metadata-group')
            .datum([]).call(chartTooltip)
        }
      })
    })

    /**
     * Indica un grafico ad area con la struttura dati fornita da l'url
     * indicato in data-areachart, via json.
     *
     */
    $('[data-areachart]', this).each(function () {
      const $this = $(this)
      const areaContainer = d3selection.select(this)
      const containerWidth = areaContainer.node()
        ? areaContainer.node().getBoundingClientRect().width : false
      const areaChart = stackedArea()

      areaChart
        .tooltipThreshold(600)
        .width(containerWidth)
        .grid('full')
        .xAxisFormat('custom')
        .xAxisCustomFormat('%d/%m/%Y')
      // .on('customMouseOver', chartTooltip.show)
      // .on('customMouseMove', chartTooltip.update)
      // .on('customMouseOut', chartTooltip.hide)

      $.getJSON($this.data('areachart'), function (dataset) {
        if (areaContainer.datum() === undefined) {
          areaContainer.datum(dataset).call(areaChart)
        }
      })
    })

    $('time', this).timeago()
    $('time', this).tooltip({placement: 'bottom'})

    $('[data-ko-route]', this).trigger('dom:improve')
  } // end .improve

  // Attiva prima possibile le personalizzazioni.
  $(document.body).improve()

  $('select[data-selectize-depending-select]').on('change', function (e) {
    const $this = $(this)

    const dependingSelectId = $this.data('selectize-depending-select')
    const dependingSelect = $('#' + dependingSelectId)

    if (dependingSelect != null) {
      const dependingMap = JSON.parse($this.data('selectize-depending-map')
        .replace(/'/g, '"'))
      if (dependingMap[$this.select2('val')] != null) {
        dependingSelect.select2('val', dependingMap[$this.select2('val')])
      }
    }
    e.preventDefault()
  })

  $('.modal button[data-related]').click(function (e) {
    var $modal = $(this).parents('.modal')
    var $form = $modal.find('form')
    var $related = $(this).data('related')

    $.ajax({
      type: $form.attr('method'),
      url: $form.attr('action'),
      data: $form.serialize(),
      success: function (data, status, h) {
        var $control = $($related)
        var sdata = {id: data.value, text: data.label}

        $control.select2('destroy').select2({data: function () {
          return { results: [sdata] }
        }}).select2('data', sdata).trigger('change')

        $modal.modal('hide')
        // TODO: clear modal data
      },
      error: function (jqXHR, status, error) {
        $form.html(jqXHR.responseText).improve()
      }
    })

    e.preventDefault()
  })

  /**
   * Ritrova la posizione GPS corrente dal browser e la invia con una PUT
   * all'indirizzo indicato in href.
   * Si mostra il popover appena la locazione geografica è stata passata
   * con successo al server via ajax.
   */
  $(document.body).on('click', 'a[data-geolocation-remote]', function (e) {
    var $this = $(this)
    var key = $this.data('geolocationRemote')
    function setPosition (position) {
      var coords = '' + position.coords.latitude + ',' + position.coords.longitude
      var data = {}
      data[key] = coords
      $.ajax({
        url: $this.attr('href'),
        data: data,
        type: 'PUT'
      }).done(function (data, status) {
        $this.popover('show')
      })
    }
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(setPosition)
    }

    e.preventDefault()
  })

  $(document.body).on('click', '[data-geolocation]', function (e) {
    const $this = $(this)
    const $target = $($this.data('geolocation'))
    function setPosition (position) {
      $target.val('' + position.coords.latitude + ',' + position.coords.longitude)
      $this.popover('show')
    }
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(setPosition)
    }
    e.preventDefault()
  })

  // ++ Doctors/addCustomerAddress
  $(document.body).on('change', ':input[data-dependent][data-independent]', function (e) {
    const $this = $(this)
    const $dependent = $($this.data('dependent'))
    $dependent.prop('disabled', !$this.val())
    // attr per il link
    $($this.data('independent')).attr('disabled', !!$this.val())
  })
  // -- addCustomerAddress

  // ++ VisitToDoctors
  $(document.body).on('change', '[data-workdays-only]', function (e) {
    const $this = $(this)
    const value = moment($this.val(), 'DD/MM/YYYY')
    if (value.isValid() && (value.isoWeekday() === 6 || value.isoWeekday() === 7)) {
      $this.popover('show')
    } else {
      $this.popover('hide')
    }
  })

  // rimuove gli spazi bianchi nello username (vedere #967)
  $(document.body).on('submit', 'form', function () {
    if (window.location.pathname === '/secure/login') {
      var $username = $('input[name=username]')
      $username.val($username.val().replace(/ /g, ''))
    }
  })

  /*
   * Ricarica un segmento di una pagina in funzione del cambiamento di
   * input di una form; da notare come il post/get viene fatto su un url
   * diverso da quello della form corrent.
   * Il data-control-url è utilizzato per inviare _tutta_ questa form,
   * dal cui risultato viene prelevato solo il data-control-target.
   */
  $(document.body).on('change', ':input[data-control-target]', function (e) {
    var $this = $(this)
    var target = $this.data('control-target')
    var $target = $(target)
    var url = $this.data('control-url')
    var $form = $this.closest('form')

    $target.standBy()
    $.ajax({
      type: $form.attr('method'),
      url: url,
      data: $form.serialize()
    }).done(function (data, status) {
      $(target).replaceAndImprove(target, data)
    })
    e.preventDefault()
  })

  // Gestione via template mustache.
  //
  // sul button con data-template c'è il selettore per il mustache template.
  //  Esempio di uso:
  // <button class="btn btn-default" data-duplicate="details" data-template="#mytemplate" type="button">
  //   Aggiungi
  // </button>
  $(document.body).on('click', 'button[data-duplicate]', function () {
    const target = $(this).data('duplicate')
    const template = $(this).data('template')
    const size = $('[data-duplicated=' + target + ']').size()
    const $new = $(template).mustache({index: size})
    const $row = $(this).parents('.row')
    $row.before($new)
    $new.improve()
  })

  $(document.body).on('click', '[data-duplicated] button[data-remove]', function (e) {
    var $button = $(this)
    var msg = $button.data('remove')
    bootbox.confirm(msg, function (result) {
      if (result) {
        $button.parents('[data-duplicated]').addClass('hidden')
          .find('select').val(null)
      }
    })
    e.preventDefault()
  })
  // /Gestione via template mustache.
})
