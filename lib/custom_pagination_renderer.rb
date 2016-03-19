class CustomPaginationRenderer < WillPaginate::ActionView::LinkRenderer

  def html_container(html)
    tag(:nav, tag(:ol, html), class: 'pagination-a')
  end

  def page_number(page)
    unless page == current_page
      tag(:li, link(page, page, :rel => rel_value(page)))
    else
      tag(:li, link(page, 'javascript:;'), :class => 'active')
    end
  end

  def previous_or_next_page(page, text, classname)
    if page
      tag(:li, link(text, page, :class => classname))
    else
      #tag(:li, link(text, 'javascript:;', :class => 'disabled'))
    end
  end

end