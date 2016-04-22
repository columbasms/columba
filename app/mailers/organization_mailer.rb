class OrganizationMailer < ApplicationMailer
  default from: 'no-reply@columbasms.com',
          reply_to: 'columbasms@gmail.com'

  def account_locked(organization)
    @organization = organization
    mail(to: @organization.email, subject: 'Il tuo account è in attesa di approvazione')
  end

  def account_created(organization)
    @organization = organization
    mail(to: 'columbasms@gmail.com', reply_to: @organization.email, subject: 'Una nuova organizzazione ha richiesto un account')
  end

end
