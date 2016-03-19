class ContactMailer < ApplicationMailer

  def contact_email(contact_params)
    @contact = contact_params
    mail(to: 'info@columbasms.com', from: 'no-reply@columbasms.com', subject: "Richiesta di contatto da #{@contact[:name]}")
  end

end
