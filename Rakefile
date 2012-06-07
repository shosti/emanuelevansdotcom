css_dir = 'site/css'
site_stylesheet = css_dir + '/style.css'

directory 'site'
directory css_dir

desc 'Generate the html for the site'
task :html => 'site' do
  sh 'lein run'
end

desc 'Copy static assets to site'
task :assets do
  sh 'rsync -av resources/assets/ site --exclude=".*"'
end

desc 'Compile scss to css'
file site_stylesheet => ['resources/scss/style.scss', css_dir] do |t|
  sh "scss #{t.prerequisites[0]} #{t.name} --style compressed"
end

desc 'Deploy website to S3'
task :deploy do
  sh 's3cmd sync site/ s3://www.emanuelevans.com --exclude=".DS_Store" --cf-invalidate'
end

desc 'Clean site directory'
task :clean do
  rm_rf 'site'
end

desc 'Preview site'
task :preview => :build_site do
  sh 'open site/about.html'
end

desc 'Build site'
task :build_site => [site_stylesheet, :assets, :html]

task :default => :build_site
