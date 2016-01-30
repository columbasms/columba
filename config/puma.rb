lowlevel_error_handler do |e|
  [500, {}, [e.pretty_print_inspect]]
end