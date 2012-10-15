statusClasses = ['error', 'success']

flashMessage = (status, message, form) ->
  if status in statusClasses
    $('<p></p>')
      .addClass('hidden ' + status)
      .text(message)
      .insertAfter(form)
      .fadeIn()
      .delay(1500)
      .fadeout()

ajaxifyForms = ->
  if $.support.cors or XDomainRequest?
    $('form').submit (event) ->
      event.preventDefault()
      this.reset()
      $.post(
        $(this).attr('action') + '.json'
        $(this).serialize()
        (data, textStatus, jqXHR) ->
          flashMessage(data.status, data.message, $(this))
      )

$ ->
  status = getParam('status')
  message = getParam('message')

  #FIXME
  flashMessage(status, message, $('#subsform')) if status and message

  ajaxifyForms()
