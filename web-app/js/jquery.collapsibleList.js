/*!
 * jQuery Collapsible List Plugin v0.2
 * http://github.com/sergiocampama/
 *
 * Based on the Collapsible library by Stephen Morley
 * http://code.stephenmorley.org/javascript/collapsible-lists/
 *
 * Released under the MIT License
 *
 * Date: Mon Oct 15 2012
 */

var cookieName = 'collapsibleListItems';

(function($) {
	$.fn.collapsibleList = function(open) {
        this.find("li").each(function(i, li) {
			var $this = $(li);
			if($this.children("ul").length > 0) {
                var currentDir = $(this).children("span").length > 0?$(this).children("span").text():$(this).clone()    //clone the element
                    .children() //select all the children
                    .remove()   //remove all the children
                    .end()  //again go back to selected element
                    .text();
                var dirPath = '';

                $(this).parents("li.directory").each(function () {
                    var name = $(this).children("span").length > 0?$(this).children("span").text():$(this).clone()    //clone the element
                        .children() //select all the children
                        .remove()   //remove all the children
                        .end()  //again go back to selected element
                        .text();
                    dirPath = dirPath + '-' + name;
                });
                var md5Val = md5(currentDir+dirPath);

                if (readCookie(cookieName) != null && readCookie(cookieName).indexOf(md5Val) >= 0) {
                    $this.addClass("collapsibleListClosed") // Cookie for this directory found, closes this item
                } else {
                    $this.addClass("collapsibleListOpen") // else opens the item
                }

//                if(open) {
//                    $this.addClass("collapsibleListOpen")
//                } else {
//				    $this.addClass("collapsibleListClosed")
//                }
			}
			$this.children("ul")
				.addClass("collapsibleList")
				.children("li")
				.last()
				.addClass("lastChild");
			
			$this.on('mousedown', function(e){
				//Prevents selection of text on subsequent clicks
				e.preventDefault();
			})
			.click(function(e){
				//Prevents clicks from activating parent lis
				e.stopPropagation();
				if($(this).children("ul").length > 0) {
                    var currentDir = $(this).children("span").length > 0?$(this).children("span").text():$(this).clone()    //clone the element
                        .children() //select all the children
                        .remove()   //remove all the children
                        .end()  //again go back to selected element
                        .text();
                    var dirPath = '';

                    $(this).parents("li.directory").each(function () {
                        var name = $(this).children("span").length > 0?$(this).children("span").text():$(this).clone()    //clone the element
                            .children() //select all the children
                            .remove()   //remove all the children
                            .end()  //again go back to selected element
                            .text();
                        dirPath = dirPath + '-' + name;
                    });
                    var md5Val = md5(currentDir+dirPath);

                    var itemClosed = false;
                    var cookieValues = readCookie(cookieName);
                    if (cookieValues != null && cookieValues.indexOf(md5Val) >= 0) {
                        itemClosed = true;
                    }

                    if ($(this).attr('class').indexOf('collapsibleListOpen') >= 0) {
                        if (!itemClosed) {
                            var oldValues = (cookieValues != null)?cookieValues:'';
                            var newValues = oldValues + md5Val + encodeURIComponent(',');
                            createCookie(cookieName,newValues, 180); // creates new cookie
                        }
                    } else if(itemClosed) {
                        cookieValues = cookieValues.replace(md5Val+encodeURIComponent(','),'');
                        if (cookieValues.length > 0) {
                            createCookie(cookieName,cookieValues, 180); // creates new cookie without this value
                        } else {
                            eraseCookie(cookieName) // erases cookie completely because no value is left
                        }
                    }

					$(this).toggleClass("collapsibleListClosed collapsibleListOpen");
				}
			});
		});
        
		return this;
	}
})(jQuery);

function createCookie(name,value,days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        var expires = "; expires="+date.toGMTString();
    }
    else var expires = "";
    document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function eraseCookie(name) {
    createCookie(name,"",-1);
}