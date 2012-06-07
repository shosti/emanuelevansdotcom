css_dir = 'site/css'
site_stylesheet = css_dir + '/style.css'

directory css_dir

desc 'Generate the html for the site'
task :html do
  sh 'lein run'
end

def sync_dirs(src, dest)
  src_files = Dir.glob("#{src}/*")
  dest_files = Dir.glob("#{dest}/*")

  src_files.each do |f|
    target = "#{dest}/#{File.basename(f)}"
    cp f, dest unless uptodate? target, [f]
  end
end

desc 'Copy static assets to site'
task :assets do
  Dir.glob('resources/assets/*').each do |d|
    dest = 'site/' + File.basename(d)
    mkdir dest unless File.directory? dest
    sync_dirs d, dest
  end
end

desc 'Compile scss to css'
file site_stylesheet => ['resources/scss/style.scss', css_dir] do |t|
  sh "scss #{t.prerequisites[0]} #{t.name} --style compressed"
end

desc 'Deploy website to S3'
task :deploy do
  sh 's3cmd sync site/ s3://www.emanuelevans.com'
end

desc 'Clean site directory'
task :clean do
  sh 'rm -rf site/*'
end

task :default => [site_stylesheet, :assets, :html]
