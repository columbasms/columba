/*********************************************************************
 *  #### Twitter Post Fetcher v12.0 ####
 *  Coded by Jason Mayes 2013. A present to all the developers out there.
 *  www.jasonmayes.com
 *  Please keep this disclaimer with my code if you use it. Thanks. :-)
 *  Got feedback or questions, ask here:
 *  http://www.jasonmayes.com/projects/twitterApi/
 *  Github: https://github.com/jasonmayes/Twitter-Post-Fetcher
 *  Updates will be posted to this site.
 *********************************************************************/
var twitterFetcher = function() {
    var domNode = '';
    var maxTweets = 20;
    var parseLinks = true;
    var queue = [];
    var inProgress = false;
    var printTime = true;
    var printUser = true;
    var formatterFunction = null;
    var supportsClassName = true;
    var showRts = true;
    var customCallbackFunction = null;
    var showInteractionLinks = true;
    var showImages = false;
    var lang = 'en';

    function handleTweets(tweets) {
        if (customCallbackFunction === null) {
            var x = tweets.length;
            var n = 0;
            var element = document.getElementById(domNode);
            var html = '';
            while (n < x) {
                html += '<li><span class="title">' + tweets[n] + '</li>';
                n++;
            }
            html += '';
            element.innerHTML = html;
        } else {
            customCallbackFunction(tweets);
        }
        $('.tweets-a').each(function() {
            $(this).clone().addClass('regular-only').insertAfter($(this)).clone().removeClass('regular-only').addClass('desktop-hide').insertAfter($(this));
        });
        $('.tweets-a').wrapInner('<div class="inner"></div>');
        $('.tweets-a li').wrap('<div class="wrapper"></div>');
        $('.tweets-a:not(.regular-only, .desktop-hide) > .inner').bxSlider({pager: true, controls: false, useCSS: false, adaptiveHeight: true, slideWidth: 340, minSlides: 1, maxSlides: 3, moveSlides: 3, slideMargin: 90, auto: true, pause: 5000});
        $('.tweets-a.regular-only > .inner').bxSlider({pager: true, controls: false, useCSS: false, adaptiveHeight: true, slideWidth: 300, minSlides: 1, maxSlides: 3, moveSlides: 3, slideMargin: 40, auto: true, pause: 5000});
        $('.tweets-a.desktop-hide > .inner').bxSlider({pager: true, controls: false, useCSS: false, adaptiveHeight: true});

        $('#twitter').wrapInner('<div class="inner"></div>');
        $('#twitter li').wrap('<div></div>');
        $('#twitter > .inner').each(function(){ $(this).bxSlider({ pager: false, controls: true, useCSS: false, adaptiveHeight: true }); });
    }

    function strip(data) {
        return data.replace(/<b[^>]*>(.*?)<\/b>/gi, function(a, s) {
            return s;
        })
                .replace(/class=".*?"|data-query-source=".*?"|dir=".*?"|rel=".*?"/gi,
                        '');
    }

    function getElementsByClassName(node, classname) {
        var a = [];
        var regex = new RegExp('(^| )' + classname + '( |$)');
        var elems = node.getElementsByTagName('*');
        for (var i = 0, j = elems.length; i < j; i++) {
            if (regex.test(elems[i].className)) {
                a.push(elems[i]);
            }
        }
        return a;
    }

    function extractImageUrl(image_data) {
        if (image_data !== undefined) {
            var data_src = image_data.innerHTML.match(/data-srcset="([A-z0-9%_\.-]+)/i)[0];
            return decodeURIComponent(data_src).split('"')[1];
        }
    }

    return {
        fetch: function(config) {
            if (config.maxTweets === undefined) {
                config.maxTweets = 20;
            }
            if (config.enableLinks === undefined) {
                config.enableLinks = true;
            }
            if (config.showUser === undefined) {
                config.showUser = true;
            }
            if (config.showTime === undefined) {
                config.showTime = true;
            }
            if (config.dateFunction === undefined) {
                config.dateFunction = 'default';
            }
            if (config.showRetweet === undefined) {
                config.showRetweet = true;
            }
            if (config.customCallback === undefined) {
                config.customCallback = null;
            }
            if (config.showInteraction === undefined) {
                config.showInteraction = true;
            }
            if (config.showImages === undefined) {
                config.showImages = false;
            }

            if (inProgress) {
                queue.push(config);
            } else {
                inProgress = true;

                domNode = config.domId;
                maxTweets = config.maxTweets;
                parseLinks = config.enableLinks;
                printUser = config.showUser;
                printTime = config.showTime;
                showRts = config.showRetweet;
                formatterFunction = config.dateFunction;
                customCallbackFunction = config.customCallback;
                showInteractionLinks = config.showInteraction;
                showImages = config.showImages;

                var script = document.createElement('script');
                script.type = 'text/javascript';
                script.src = '//cdn.syndication.twimg.com/widgets/timelines/' +
                        config.id + '?&lang=' + (config.lang || lang) + '&callback=twitterFetcher.callback&' +
                        'suppress_response_codes=true&rnd=' + Math.random();
                document.getElementsByTagName('head')[0].appendChild(script);
            }
        },
        callback: function(data) {
            var div = document.createElement('div');
            div.innerHTML = data.body;
            if (typeof (div.getElementsByClassName) === 'undefined') {
                supportsClassName = false;
            }

            var tweets = [];
            var authors = [];
            var authors_urls = [];
            var nicknames = [];
            var times = [];
            var images = [];
            var rts = [];
            var tids = [];
            var x = 0;

            if (supportsClassName) {
                var tmp = div.getElementsByClassName('tweet');

                while (x < tmp.length) {
                    if (tmp[x].getElementsByClassName('retweet-credit').length > 0) {
                        rts.push(true);
                    } else {
                        rts.push(false);
                    }
                    if (!rts[x] || rts[x] && showRts) {

                        var author = tmp[x].getElementsByClassName('p-author')[0].getElementsByClassName('full-name')[0];
                        var nickname = tmp[x].getElementsByClassName('p-author')[0].getElementsByClassName('p-nickname')[0];
                        var author_url = tmp[x].getElementsByClassName('p-author')[0].getElementsByTagName('a')[0].getAttribute("href");

                        tweets.push(tmp[x].getElementsByClassName('e-entry-title')[0]);
                        tids.push(tmp[x].getAttribute('data-tweet-id'));
                        authors.push(author);
                        nicknames.push(nickname);
                        authors_urls.push(author_url);
                        times.push(tmp[x].getElementsByClassName('dt-updated')[0]);
                        if (tmp[x].getElementsByClassName('u-photo')[0].getAttribute('data-src-2x') !== undefined) {
                            images.push(tmp[x].getElementsByClassName('u-photo')[0].getAttribute('data-src-2x'));
                        } else {
                            images.push(undefined);
                        }
                    }
                    x++;
                }
            } else {
                var tmp = getElementsByClassName(div, 'tweet');
                while (x < tmp.length) {
                    tweets.push(getElementsByClassName(tmp[x], 'e-entry-title')[0]);
                    tids.push(tmp[x].getAttribute('data-tweet-id'));
                    authors.push(getElementsByClassName(tmp[x], 'p-author')[0]);
                    times.push(getElementsByClassName(tmp[x], 'dt-updated')[0]);
                    if (getElementsByClassName(tmp[x], 'inline-media')[0] !== undefined) {
                        images.push(getElementsByClassName(tmp[x], 'inline-media')[0]);
                    } else {
                        images.push(undefined);
                    }

                    if (getElementsByClassName(tmp[x], 'retweet-credit').length > 0) {
                        rts.push(true);
                    } else {
                        rts.push(false);
                    }
                    x++;
                }
            }

            if (tweets.length > maxTweets) {
                tweets.splice(maxTweets, (tweets.length - maxTweets));
                authors.splice(maxTweets, (authors.length - maxTweets));
                authors_urls.splice(maxTweets, (authors_urls.length - maxTweets));
                nicknames.splice(maxTweets, (nicknames.length - maxTweets));
                times.splice(maxTweets, (times.length - maxTweets));
                rts.splice(maxTweets, (rts.length - maxTweets));
                images.splice(maxTweets, (images.length - maxTweets));
            }

            var arrayTweets = [];
            var x = tweets.length;
            var n = 0;
            while (n < x) {
                if (typeof (formatterFunction) !== 'string') {
                    var newDate = new Date(times[n].getAttribute('datetime')
                            .replace(/-/g, '/').replace('T', ' ').split('+')[0]);
                    var dateString = formatterFunction(newDate);
                    times[n].setAttribute('aria-label', dateString);

                    if (tweets[n].innerText) {
                        // IE hack.
                        if (supportsClassName) {
                            times[n].innerText = dateString;
                        } else {
                            var h = document.createElement('p');
                            var t = document.createTextNode(dateString);
                            h.appendChild(t);
                            h.setAttribute('aria-label', dateString);
                            times[n] = h;
                        }
                    } else {
                        times[n].textContent = dateString;
                    }
                }
                var op = '';
                if (parseLinks) {
                    if (printUser) {
                        op += '<div class="user">' + strip(authors[n].innerHTML) +
                                '</div>';
                    }
                    if (printTime) {
                        op += '<span class="date">' +
                                times[n].getAttribute('aria-label') + '</span>';
                    }
                    op += '<div class="tweet">' + strip(tweets[n].innerHTML) + '</div>';

                } else {
                    if (tweets[n].innerText) {

                        if (printUser) {
                            op += '<span>' + authors[n].innerText + '</span>' + '<a href="'+ authors_urls[n] +'">' + nicknames[n].innerText + '</a>';
                        }
                        if (printTime) {

                            var time_formatted = new Date(times[n].getAttribute('datetime'));

                            var months = [
                                "January", "February", "March",
                                "April", "May", "June", "July",
                                "August", "September", "October",
                                "November", "December"
                            ];
                            var formattedDAte = months[time_formatted.getMonth()] + " " + time_formatted.getDate()+ " " + time_formatted.getFullYear()

                            op += '<span class="date">' +
                            formattedDAte + '</span></span>';
                        }
                        op += strip(tweets[n].innerHTML);
                    } else {
                        if (printUser) {
                            op += '<span>' + authors[n].innerText + '</span>' + '<a href="'+ authors_urls[n] +'">' + nicknames[n].innerText + '</a>';
                        }
                        if (printTime) { console.log(times[n]);

                            var time_formatted = new Date(times[n].getAttribute('datetime'));

                            var months = [
                                "January", "February", "March",
                                "April", "May", "June", "July",
                                "August", "September", "October",
                                "November", "December"
                            ];
                            var formattedDAte = months[time_formatted.getMonth()] + " " + time_formatted.getDate()+ " " + time_formatted.getFullYear()

                            op += '<span class="date">' +
                            formattedDAte + '</span></span>';
                        }
                        op += strip(tweets[n].innerHTML);
                    }
                }
                if (showInteractionLinks) {
                    op += '<p class="interact"><a href="https://twitter.com/intent/' +
                            'tweet?in_reply_to=' + tids[n] + '" class="twitter_reply_icon">' +
                            'Reply</a><a href="https://twitter.com/intent/retweet?tweet_id=' +
                            tids[n] + '" class="twitter_retweet_icon">Retweet</a>' +
                            '<a href="https://twitter.com/intent/favorite?tweet_id=' +
                            tids[n] + '" class="twitter_fav_icon">Favorite</a></p>';
                }

                if (showImages && images[n] !== undefined) {
                    op += '<img src="' + images[n] + '" alt="" />';
                }

                arrayTweets.push(op);
                n++;
            }
            handleTweets(arrayTweets);
            inProgress = false;

            if (queue.length > 0) {
                twitterFetcher.fetch(queue[0]);
                queue.splice(0, 1);
            }
        }
    }
}();
