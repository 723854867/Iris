'use strict';

var gulp = require('gulp');
var sass = require('gulp-sass');
// var jade = require('gulp-jade');
var uglify = require('gulp-uglify');
var minCss = require('gulp-minify-css');
var browserSync = require('browser-sync').create();
var reload      = browserSync.reload;

gulp.task('sass', function () {
  return gulp.src('./src/sass/*.scss')
    .pipe(sass().on('error', sass.logError))
    .pipe(gulp.dest('./dist/css'))
    .pipe(reload({stream: true}));
});


gulp.task('minScript',function(){
	return gulp.src('./src/js/*.js')
	.pipe(uglify())
	.pipe(gulp.dest('./dist/js'));
})



// gulp.task('watch', function () {

//   gulp.watch('./src/sass/*.scss',['sass']);
//   gulp.watch('./src/js/*.js', ['minScript']);

// });

gulp.task('default',['sass'],function(){
    // gulp.start('sass', 'minScript');

    browserSync.init({
        server: "./"
    });
     gulp.watch("./src/sass/*.scss", ['sass']);
     gulp.watch('./src/js/*.js', ['minScript']);
    gulp.watch("./*.html").on('change', reload);
})




