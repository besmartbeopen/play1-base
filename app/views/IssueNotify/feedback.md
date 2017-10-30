${data.note}

- *URL:* ${data.url}#{if operator.present}
- *Operatore:* ${operator.get()}#{/if}#{if sudo.present}
- *Operatore reale:* ${sudo.get()}#{/if}
- *User Agent:* ${data.browser.userAgent}
- *IP:* ${clientIp}
- *Session:*
#{list items:session.all(), as:'item'}#{if !item.key.startsWith('__')}
 - **${item.key}** ${item.value}#{/if}#{/list}

${subscriber}
