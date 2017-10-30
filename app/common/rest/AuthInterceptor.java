package common.rest;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author marco
 *
 */
public class AuthInterceptor implements RequestInterceptor {

  private static final String AUTHORIZATION = "Authorization";
  private static final String BEARER = "Bearer ";

  private final String token;

  public AuthInterceptor(String token) {
    this.token = token;
  }

  @Override
  public void apply(RequestTemplate template) {
    template.header(AUTHORIZATION, BEARER + token);
  }
}
