css_dir = 'site/css'
site_stylesheet = css_dir + '/style.css'

directory 'site'
directory css_dir

desc 'Generate the html for the site'
task :html => 'site' do
  gen_files = Dir.glob('src/emanuelevansdotcom/*') +
    Dir.glob('resources/pages/*')
  sh 'lein run' unless uptodate? 'site/about.html', gen_files
end

desc 'Encode ogg versions of mp3 files'
task :encode_ogg do
  Dir.glob('resources/assets/audio/*.mp3').each do |mp3_file|
    ogg_file = mp3_file.split('.mp3')[0] + '.ogg'
    unless uptodate? ogg_file, [mp3_file]
      sh "ffmpeg -i #{mp3_file} -acodec libvorbis -aq 6 #{ogg_file}"
    end
  end
end

desc 'Copy static assets to site'
task :assets => :encode_ogg do
  sh 'rsync -a resources/assets/ site --exclude=".*"'
end

desc 'Compile scss to css'
file site_stylesheet => ['resources/scss/style.scss', css_dir] do |t|
  sh "scss #{t.prerequisites[0]} #{t.name} --style compressed"
end

desc 'Deploy website to S3'
task :deploy => :build_site do
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
