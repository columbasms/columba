class SpamController < ApplicationController
  # ad the base service is intended for mail, to avoid result noise because of lack of email headers we put in a
  # safe header so to make the resulting score depending only on body text

  # returns the spam score of a given message
  def check_spam_score(text)
    pre="MIME-Version: 1.0
Received: by 10.112.181.74 with HTTP; Wed, 16 Mar 2016 12:10:47 -0700 (PDT)
Date: Wed, 16 Mar 2016 20:10:47 +0100
Delivered-To: columbasms@gmail.com
Message-ID: <CAH6wr0GHfy_J5UYhCJ5zuKLEJPXM0sOJb8EA3FfzWnFFJc4qVw@mail.gmail.com>
Subject: Benvenuti in Columba!
From: Columba SMS <columbasms@gmail.com>
To: onlus@onlus.it
Content-Type: multipart/alternative; boundary=14dae93d930a752b75052e2f45ab

--14dae93d930a752b75052e2f45ab
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: quoted-printable"

    post="--14dae93d930a752b75052e2f45ab--"
    a=Postmark::SpamCheck.check(""+pre+""+text+""+post, report_format = :long, timeout = 60)
    # a=Postmark::SpamCheck.check(text, report_format = :long, timeout = 60)

    return a.score
  end


end
