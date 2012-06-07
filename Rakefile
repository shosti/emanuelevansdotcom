css_dir = 'site/css'
site_stylesheet = css_dir + '/style.css'

directory css_dir

desc 'Generate the html for the site'
task :html do
  sh 'lein run'
end

desc 'Copy static assets to site'
task :assets do
  Dir.glob('resources/assets/*').each do |d|
    dest = 'site/' + File.basename(d)
    mkdir dest unless File.directory? dest
    Dir.glob(d + '/*').each do |f|
      fname = File.basename(f)
      target = "#{dest}/#{fname}"
      cp f, dest unless uptodate? target, [f]
    end
  end
end

desc 'Compile scss to css'
file site_stylesheet => ['resources/scss/style.scss', css_dir] do |t|
  sh "scss #{t.prerequisites[0]} #{t.name} --style compressed"
end

task :clean do
  sh 'rm -rf site/*'
end

task :default => [site_stylesheet, :assets, :html]
