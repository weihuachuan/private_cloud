var request = require('request');
request = request.defaults({jar: true})

var j = request.jar()
var fs = require("fs");

var path=require("path");
var http=require("http");

 var options = {
        url: 'http://192.168.137.128:9080/loginPost',
        form: {
            username:"Superadmin",
            password:"superadmin"
        }  
    };

    request.post(options, function(error, response, body) {

        console.info('response:' + JSON.stringify(response));
        console.info("statusCode:" + response.statusCode);
          console.info("headers:" + JSON.stringify(response.headers));
        console.info('body: ' + body );

var cookie_string = j.getCookieString(options.url);

console.log(cookie_string);

var formData = {
 // 键-值对简单值
 bucket: 'photo2',
 // 使用 Buffers 添加数据
 key: '/dir1/dir2/addUser.html',
 // 使用 Streams 添加数据
 // 通过数组添加 multiple 值
 mediaType: 'html',
 // 添加可选的 meta-data 使用: {value: DATA, options: OPTIONS}
 // 对于一些流类型，需要提供手工添加 "file"-关联 
 // 详细查看 `form-data` : https://github.com/form-data/form-data
 custom_file: {
 content: fs.readFileSync('d:/addUser.html','utf-8'),
 options: {
 filename: 'addUser.html',
 contentType: 'html'
 }
 }
};
request.post({url:'http://192.168.137.128:9080/hos/v1/object', formData: formData}, function optionalCallback(err, httpResponse, body) {
 if (err) {
 return console.error('upload failed:', err);
 }
 console.log(' Server responded with:', body);
});









});









