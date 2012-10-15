statuses = ['error', 'success']

flashMessage = (status, message, form) ->
  if status in statuses
    $('<p></p>')
      .addClass('hidden ' + status)
      .text(message)
      .insertAfter(form)
      .fadeIn()
      .delay(1500)
      .fadeOut()

ajaxifyForms = ->
  if $.support.cors or $.support.iecors
    $('form').submit (event) ->
      form = this
      action = $(form).attr('action') + '.json'
      data = $(form).serialize()
      form.reset()
      event.preventDefault()
      $.post(
        action
        data
        (data, textStatus, jqXHR) ->
          flashMessage(data.status, data.message, $(form))
      )

$ ->
  status = getParam('status')
  message = getParam('message')

  #FIXME
  flashMessage(status, message, $('#subsform')) if status and message

  ajaxifyForms()
