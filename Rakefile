css_dir = 'site/css'
site_stylesheet = css_dir + '/style.css'

directory 'site'
directory css_dir

gzip_exts = ['html', 'css', 'js']
gz_deploy_dir = 'deploy/gz'
ungz_deploy_dir = 'deploy/ungz'
directory gz_deploy_dir
directory ungz_deploy_dir

desc 'Generate the html for the site'
task :html => 'site' do
  gen_files = Dir.glob('src/emanuelevansdotcom/*') +
    Dir.glob('resources/pages/*') + Dir.glob('resources/audio/*')
  sh 'lein run' unless uptodate? 'site/about.html', gen_files
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

def process_unsubscriptions
  unsubs_cmd = `mu find maildir:/Unsubscriptions -f f 2> /dev/null`
  unsubs = unsubs_cmd.split("\n").map {|s| /<(.*)>/.match(s)[1]}
  unsubs.each do |addr|
    locs = `mu find maildir:/Subscriptions from:#{addr} -f l 2> /dev/null`
    locs.split("\n").each do |loc|
      rm loc
    end
  end
  `mu find maildir:/Unsubscriptions -f l 2> /dev/null`.split("\n").each do |loc|
    rm loc
  end
end

desc 'Make mailing list from maildir'
task :make_maillist do
  sh "mu index --autoupgrade > /dev/null"
  process_unsubscriptions
  maillist = `mu find maildir:/Subscriptions -f f 2> /dev/null`
  File.open('resources/mail/maillist', 'w') { |f| f.write maillist}
end

desc 'Send maillist messages'
task :mail => :make_maillist do
  sh "lein run -m emanuelevansdotcom.mail"
end

desc 'Copy static assets to site'
task :assets => [:encode_audio, :optimize_img] do
  sh 'rsync -a resources/assets/ site --exclude=".*"'
end

desc 'Compile scss to css'
file site_stylesheet => ['resources/scss/style.scss', css_dir] do |t|
  sh "scss #{t.prerequisites[0]} #{t.name} --style compressed"
end

desc 'Copy gzipped static assets to their own folder'
task :gz => [:build_site, gz_deploy_dir] do
  filter_rules = gzip_exts.map {|ext| "--include '*.#{ext}'"}.join " "
  sh "rsync -a site/ #{gz_deploy_dir} --include '*/' #{filter_rules} --exclude '*'"
  base_dir = Dir.getwd
  (gzip_exts + ['']).map {|s| "#{base_dir}/deploy/gz/#{s}"}.each do |dir|
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
  sh "rsync -a site/ #{ungz_deploy_dir} #{filter_rules}"
end

desc 'Deploy website to S3'
task :deploy => [:ungz, :gz] do
  sh 's3cmd sync deploy/ungz/ s3://www.emanuelevans.com --exclude=".DS_Store"'
  sh 's3cmd sync deploy/gz/ s3://www.emanuelevans.com --exclude=".DS_Store" --add-header "Content-Encoding: gzip"'
end

desc 'Clean site directory'
task :clean do
  rm_rf 'site'
  rm_rf 'deploy'
end

desc 'Preview site'
task :preview => :build_site do
  sh 'open site/about.html'
end

desc 'Build site'
task :build_site => [site_stylesheet, :assets, :html]

task :default => :build_site
