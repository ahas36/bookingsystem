



  $(document).ready(function() {
    $('#calendar').weekCalendar({
      date:dts,
      timeslotsPerHour: 6,
      timeslotHeigh: 30,
      hourLine: true,
      readonly:true,
      data: eventData,
      height: function($calendar) {
        return $(window).height() - $('h1').outerHeight(true);
      },
      eventRender : function(calEvent, $event) {
        if (calEvent.end.getTime() < new Date().getTime()) {
          $event.css('backgroundColor', '#aaa');
          $event.find('.time').css({'backgroundColor': '#999', 'border':'1px solid #888'});
        }
      }
    });

  });

