class OrganizationMailer < ApplicationMailer
  default from: 'no-reply@columbasms.com',
          reply_to: 'info@columbasms.com'

  def account_locked(organization)
    @organization = organization
    mail(to: @organization.email, subject: 'Il tuo account è in attesa di approvazione')
  end

  def account_created(organization)
    @organization = organization
    mail(to: 'info@columbasms.com', reply_to: @organization.email, subject: 'Una nuova organizzazione ha richiesto un account')
  end

end
