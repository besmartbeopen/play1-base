%{
  activity = _arg
}%
#{doBody /}
${activity.startAt.toDate().format()}#{if !activity.isOneDay()} - ${activity.endTo.toDate().format()}#{/if}#{else},
&{'PartOfTheDay.'+activity.partOfTheDay}#{/else}