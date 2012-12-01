css_dir = '_site/css'
site_stylesheet = css_dir + '/style.css'

directory '_site'
directory css_dir
directory '_site/js'

gzip_exts = ['html', 'css', 'js', 'txt', 'ico']
gz_deploy_dir = '_deploy/gz'
ungz_deploy_dir = '_deploy/ungz'
directory gz_deploy_dir
directory ungz_deploy_dir

desc 'Generate the html for the site'
task :html => '_site' do
  gen_files = Dir.glob('src/emanuelevansdotcom/*') +
    Dir.glob('resources/pages/*') + Dir.glob('resources/audio/*')
  sh 'lein with-profile dev run' unless uptodate? '_site/about.html', gen_files
end

desc 'Encode mp3 and ogg versions of audio files'
task :encode_audio do
  Dir.glob('resources/audio/*.aiff').each do |source_file|
    out_dir = 'resources/assets/audio/'
    mp3_file = out_dir + File.basename(source_file, 'aiff') + 'mp3'
    ogg_file = out_dir + File.basename(source_file, 'aiff') + 'ogg'
    unless uptodate? mp3_file, [source_file]
      sh "ffmpeg -i #{source_file} -acodec libmp3lame -ac 1 -ab 64k #{mp3_file}"
    end
    unless uptodate? ogg_file, [source_file]
      sh "ffmpeg -i #{source_file} -acodec libvorbis -ac 1 #{ogg_file}"
    end
  end
end

desc 'Optimize images'
task :optimize_img do
  sh "jpegoptim --strip-all --quiet resources/assets/images/*.jpg"
  sh "optipng -quiet resources/assets/images/*.png"
end

desc 'Fetch calendar data from gcal'
task :fetch_cal do
  sh "lein run -m emanuelevansdotcom.cal"
end

desc 'Send maillist messages'
task :mail do
  sh "lein run -m emanuelevansdotcom.mail"
end

desc 'Copy static assets to site'
task :assets => [:encode_audio, :optimize_img] do
  sh 'rsync -a resources/assets/ _site --exclude=".*"'
end

desc 'Compile scss to css'
file site_stylesheet => ['resources/scss/style.scss', css_dir] do |t|
  sh "scss #{t.prerequisites[0]} #{t.name} --style compressed"
end


desc 'Combine and minify js'
task :js => '_site/js' do
  out_file = '_site/js/scripts.js'
  sh "coffee -c resources/coffee/*.coffee"
  sh "cat resources/js/*.js resources/coffee/*.js > scripts.max.js"
  sh "uglifyjs scripts.max.js > _site/js/scripts.js"
  rm 'scripts.max.js'
end

desc 'Copy gzipped static assets to their own folder'
task :gz => [:build_site, gz_deploy_dir] do
  filter_rules = gzip_exts.map {|ext| "--include '*.#{ext}'"}.join " "
  sh "rsync -a _site/ #{gz_deploy_dir} --include '*/' #{filter_rules} --exclude '*'"
  base_dir = Dir.getwd
  (gzip_exts + ['']).map {|s| "#{base_dir}/_deploy/gz/#{s}"}.each do |dir|
    if File.directory? dir
      cd dir
      Dir.glob('*.*').each do |f|
        sh "gzip -9 #{f}"
        sh "mv #{f}.gz #{f}"
      end
    end
  end
  cd base_dir
end

desc 'Copy non-gzipped static assets to their own folder'
task :ungz => [:build_site, ungz_deploy_dir] do
  filter_rules = gzip_exts.map {|ext| "--exclude '*.#{ext}'"}.join " "
  sh "rsync -a _site/ #{ungz_deploy_dir} #{filter_rules}"
end

require 'time'
task :s3 => [:ungz, :gz] do
  expiration_date = (Time.utc(Time.now.year + 2))
  exp_header = "Expires: #{expiration_date.rfc822}"
  cache_header = "Cache-Control: max-age=#{60 * 60 * 24 * 365}, public"
  headers = "--add-header '#{exp_header}' --add-header '#{cache_header}'"
  sh "s3cmd sync #{ungz_deploy_dir}/ s3://www.emanuelevans.com --exclude='.DS_Store' #{headers}"
  sh "s3cmd sync #{gz_deploy_dir}/ s3://www.emanuelevans.com --exclude='.DS_Store' --add-header 'Content-Encoding: gzip'"
end

desc 'Clean site directory'
task :clean do
  rm_rf '_site'
  rm_rf '_deploy'
end

desc 'Preview site'
task :preview => :build_site do
  sh 'open _site/about.html'
end

desc 'Build site'
task :build_site => [site_stylesheet, :js, :assets, :html]

task :deploy => :s3
task :default => :build_site
