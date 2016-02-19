class CustomRenderer < SimpleNavigation::Renderer::Base
  def render(item_container)
    list_content = item_container.items.inject([]) do |list, item|
      li_span = content_tag 'span', item.name, class: 'title'
      li_span += content_tag('span', '', class: "arrow #{'open active' if item.selected?}") if item.sub_navigation.present?
      li_url = (item.nil? or item.url == '#') ? 'javascript:;' : item.url
      li_content = link_to(li_span, li_url)
      li_icon = content_tag 'i', '', class: item.html_options[:icon_class]
      li_content += content_tag 'span', li_icon, class: "icon-thumbnail #{'bg-success' if item.selected?}"

      if item.sub_navigation.present?
        li_content += render_sub_navigation_for(item)
      end
      list << content_tag(:li, li_content, class: "#{item.html_options[:li_class]} #{'open active' if item.selected?}")
    end.join
    if skip_if_empty? && item_container.empty?
      ''
    else
      content_tag((options[:ordered] ? :ol : :ul), list_content, {:id => item_container.dom_id, :class => item_container.dom_class})
    end
  end
end