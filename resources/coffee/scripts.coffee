statuses = ['error', 'success']

flashMessage = (status, message, form) ->
  if status in statuses
    $('<p></p>')
      .css('display', 'none')
      .addClass(status)
      .text(message)
      .insertAfter(form)
      .fadeIn()
      .delay(2000)
      .fadeOut()

ajaxifyForms = ->
  if $.support.cors
    $('form').submit (event) ->
      form = this
      action = $(form).attr('action') + '.json'
      formData = $(form).serialize()
      form.reset()
      event.preventDefault()
      $.post(
        action
        formData
        (data, textStatus, jqXHR) ->
          flashMessage(data.status, data.message, $(form))
        'json'
      ).error((jqXHR) ->
        flashMessage('error'
          'Website error, please try again later'
          $(form))
      )

$ ->
  status = getParam('status')
  message = getParam('message')
  action = getParam('action')

  flashMessage(status, message, $('#' + action)) if status and message

  ajaxifyForms()
