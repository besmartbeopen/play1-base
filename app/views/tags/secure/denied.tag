#{if !(session.username &&
      controllers.Secure.Security.invoke("check", _arg) &&
      (_target == null || controllers.Resecure.check(_arg, _target)))}
    #{doBody /}
#{/if}