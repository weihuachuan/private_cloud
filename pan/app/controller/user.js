var request = require('request');
request = request.defaults({jar: true})
var serverURL = '154.8.230.175';  //这里修改成你后端服务的IP
var fs = require('fs');
var _ = require('underscore');
var sID = '';


//用户注册
exports.register = function (req, res) {
    res.render('register', {});
}

exports.registerPost = function (req, res) {
    var usr = req.body.userName;
    var psd = req.body.password;
    var bucket = usr;


    var options = {
        url: 'http://127.0.0.1:8081/addUser',
        form: {
            userName: usr,
            password: psd
        }
    };

    //1.向管理员申请用户
    request.post(options, function (error, response, body) {

        console.log("我是发往" + options.url + "---" + body);
        console.log("我是发往" + options.url + "---" + error)
        console.log("查看申请用户的状态码是否成功" + JSON.parse(body).status)
        var status = parseInt(JSON.parse(body).status)

        var options2 = {
            url: 'http://' + serverURL + ':9080/loginPost',
            form: {
                username: usr,
                password: psd
            }
        };
        if (status != 200) {
            res.render('register', {status: "该用户名已被注册，请更换用户名"});
            return;
        }
        //2.注册用户并返回登录界面
        request.post(options2, function (error, response, body) {
            console.log("我是发往" + options2.url + "---" + body);
            console.log("我是发往" + options2.url + "---" + error);


            if (response.headers['set-cookie'] != undefined) {
                console.log("用户登录的时候的sID被设置了" + response.headers['set-cookie'])
                sID = response.headers['set-cookie'][0].split(';')[0].split('=')[1];
            }

            //3.创建bucket,查询用户的所有文件
            //查询bucket的返回[{"bucketId":"b9945f13f580439db67a3c44ca6a9ef2","bucketName":"photo2","creator":"SuperAdmin","detail":"ASDADA","createTime":1531477052000}]
            //创建用户的时候给一个根目录，然后登录之后，给出根目录下所有文件，然后依次递归向下查询，返回所有信息给客户端
            //思路二，，采用，，直接返回根目录，每次查询目录，在发送ajax查询
            var options1 = {
                url: 'http://' + serverURL + ':9080/hos/v1/bucket',
                form: {
                    bucket: bucket,
                    detail: "abc"
                }
            };

            request.post(options1, function (error, response, body) {
                if (response.headers['set-cookie'] != undefined) {
                    console.log("用户登录的时候的sID被设置了" + response.headers['set-cookie'])
                    sID = response.headers['set-cookie'][0].split(';')[0].split('=')[1];
                }
                let homeObj1 = JSON.stringify(response);
                console.log("-------------------------：----" + homeObj1);
                console.log("我是发往bucket创建" + "---");
                console.log("我是发往bucket创建" + "---" + error)
                console.log("此次查询的bucket： " + bucket)


            })
            res.render('login', {status: "恭喜你注册成功！"});
        })
    })

}

//用户登录
exports.login = function (req, res) {
    res.render('login', {})
}
exports.loginPost = function (req, res) {
    var usr = req.body.username;
    var psd = req.body.password;

    var options = {
        url: 'http://' + serverURL + ':9080/loginPost',
        form: {
            username: usr,
            password: psd
        }
    };
    //1.用户登录   "body":"{\"msg\":\"login error\"
    request.post(options, function (error, response, body) {

        console.log("返回的reponse的json为：" + JSON.parse(body).data);

        if (JSON.parse(body).data != "success") {
            res.render('login', {status: "用户名或密码错误"});
            return;
        }


        let homeObj = JSON.stringify(req.cookies);
        console.log("登录的时候的cookie：" + homeObj);
        console.log("返回的response.headers的为：----" + response.headers);
        if (response.headers['set-cookie'] != undefined) {
            console.log("用户登录的时候的sID被设置了" + response.headers['set-cookie'])
            sID = response.headers['set-cookie'][0].split(';')[0].split('=')[1];
        }


        //2.用户查询自己bucket
        request.get({url: 'http://' + serverURL + ':9080/hos/v1/bucket/list'}, function (error, response, body) {
            var body = eval("(" + body + ")");
            let homeObj1 = JSON.stringify(response);
            console.log("--------------------------------------：----" + homeObj1);
            if (response.headers['set-cookie'] != undefined) {
                console.log("用户登录的时候的sID被设置了" + response.headers['set-cookie'])
                sID = response.headers['set-cookie'][0].split(';')[0].split('=')[1];
            }
            //3.用户列出自己根目录
            request.get({url: 'http://' + serverURL + ':9080/hos/v1/object/list/dir?bucket=' + body[0].bucketName + "&dir=/"}, function (error, response, body) {
                console.log("我是发往object/list/dir" + "---" + body);
                console.log("我是发往object/list/dir" + "---" + error);
                var body = eval('(' + body + ')');

                console.log(body.objectList)
                res.render('mainUI', {
                    res: body.objectList,
                    username: usr
                })
            })
        })
    })
}

//主界面
exports.main = function (req, res) {
    console.log("主界面的cookie：" + req.cookies);

    request.get({url: 'http://' + serverURL + ':9080/hos/v1/bucket/list'}, function (error, response, body) {
        var body = eval("(" + body + ")");
        // console.log(body[0].bucketName);

        //3.用户列出自己根目录
        request.get({url: 'http://' + serverURL + ':9080/hos/v1/object/list/dir?bucket=' + body[0].bucketName + "&dir=/"}, function (error, response, body) {
            console.log("我是发往object/list/dir" + "---" + body);
            console.log("我是发往object/list/dir" + "---" + error);
            var body = eval('(' + body + ')');

            console.log(body.objectList)
            var objectList = body.objectList
            request.get({url: 'http://' + serverURL + ':9080/hos/v1/sys/user'}, function (error, response, body) {
                var username = JSON.parse(body).data.userName;


                res.render('mainUI', {
                    res: objectList,
                    username: username

                })

            })
        })
    })


}

//ajax查询目录信息
exports.queryDir = function (req, res) {

    let homeObj = JSON.stringify(req.cookies);
    console.log("查询的时候的cookie：" + homeObj);


    var dir = req.query.dir;
    request.get({url: 'http://' + serverURL + ':9080/hos/v1/bucket/list'}, function (error, response, body) {

        let homeObj1 = JSON.stringify(response);
        console.log("查询返回的set-cookie的为：----" + homeObj1);
        var body = eval('(' + body + ')');

        var formData = {
            bucket: body[0].bucketName,
            dir: dir
        }
        console.log("查询目录的时候发往的bucket和dir：" + body[0].bucketName + "-------" + dir);
        request.get({
            url: 'http://' + serverURL + ':9080/hos/v1/object/list/dir',
            formData: formData
        }, function (error, response, body) {
            console.log("我是发往object/list/dir" + "---" + response.headers);
            console.log("我是发往object/list/dir" + "---" + body);
            console.log("我是发往object/list/dir" + "---" + error);
            var body = eval('(' + body + ')');
            console.log("我要好好看看body" + body.objectList)
            if (body.objectList.length == 0) {
                res.json({res: dir + "abc.jpg", length: false})
            } else {
                res.json({
                    res: body.objectList,
                    length: true
                })
            }

        })
    })
}

exports.uploadPage = function (req, res) {
    //对前端传过来的Key 处理
    var key = req.query.key;
    // key = JSON.stringify(req.query);
    //当第一次进入主页面的时候key为空字符串
    if (key == undefined) {
        key = ""
    }

    console.log("返回上传页面的时候查询路径" + key);

    request.get({url: 'http://' + serverURL + ':9080/hos/v1/sys/user'}, function (error, response, body) {
        var username = JSON.parse(body).data.userName;

        console.log("上传页面渲染的用户名查询回显" + username)
        res.render('upload', {key: key, username: username});

    })
}
//这里的上传是有问题的，当上传大文件的时候，melter可能还没写完就被post读走了，应加个同步
exports.uploadPage1 = function (req, res) {
    var key = req.body.key;
    var dir = key + "/";
    var filename = req.files[0].filename;
    var mediaType = req.files[0].originalname.split(".")[1];
    key = key + "/" + req.files[0].originalname;
    var bucket = null;
    request.get({url: 'http://' + serverURL + ':9080/hos/v1/bucket/list'}, function (error, response, body) {
        var body = eval("(" + body + ")");
        console.log(body + "----------------------------------");
        bucket = body[0].bucketName;
        console.log("上传的时候查询bucket回显：" + key + "----" + mediaType + "-----" + filename);
        //  执行上传

        if (sID == '') {
            console.log('error: 没有有效sessionID');
            return;
        }
        upload_for_hos(bucket, key, mediaType, filename);

        function upload_for_hos(bucket, key, mediaType, filename) {
            var url = 'http://' + serverURL + ':9080/hos/v1/object';
            var formData = {
                // Pass a simple key-value pair
                bucket: bucket,
                // Pass data via Buffers
                key: key,
                // Pass data via Streams
                mediaType: mediaType,
                content: fs.createReadStream('././uploads/' + filename)
            };
            var headers = {
                cookie: 'JSESSIONID=' + sID
            }
            console.log('GOGOGOGOGOGO-------');


            request.post({url: url, headers: headers, formData: formData}, function (error, response, body) {
                if (!error && response.statusCode == 200) {
                    console.log("上传成功");


                    //返回上传之前的页面
                    console.log("上传完成后返回之前页面的时候，查询文件列表时所需参数回显:" + bucket + "---" + dir);
                    var formData = {
                        bucket: bucket,
                        dir: dir
                    }
                    request.get({
                        url: 'http://' + serverURL + ':9080/hos/v1/object/list/dir',
                        formData: formData
                    }, function (error, response, body) {
                        console.log("我是发往object/list/dir" + "---" + body);
                        console.log("我是发往object/list/dir" + "---" + error);
                        var body = eval('(' + body + ')');

                        console.log(body.objectList)
                        var objectList = body.objectList;

                        console.log(dir.substr(1, dir.length - 2));
                        console.log(objectList);
                        res.render('mainUI', {
                            res: objectList,
                            username: bucket,
                            nav: dir.substr(1, dir.length - 2).split("/")
                        })
                    })

                    //上传成功之后应该删除临时文件,,,关注
                    fs.exists('././uploads/' + filename, function (exist) {
                        if (exist) {
                            console.log('././uploads/' + filename);
                            fs.unlink('././uploads/' + filename, function (err) {
                                if (err) {
                                    console.error();
                                    throw err;
                                }
                                console.log('临时文件删除成功');
                            });
                        }
                    });
                    return;
                }
                console.log("错误类型 ：" + error)
                console.log("返回的数据 ：" + response.body)
                console.log("返回的数据 ：" + response.statusCode)

            })
            console.log('GOGOGOGOGOGO');
        }


    })
}


exports.download = function (req, res) {
    var key = req.query.key;

    var filename = key.split("/")[key.split("/").length - 1];
    console.log("下载的时候的bucket和 key:" + "---" + key);

    request.get({url: 'http://' + serverURL + ':9080/hos/v1/bucket/list'}, function (error, response, body) {
        var body = eval("(" + body + ")");
        //buketname
        bucket = body[0].bucketName;
        console.log("下的的时候，必要的参数回显：" + body[0].bucketName + "-----------------" + key);
        download_for_hos(bucket, key, filename);

        //创建download方法
        function download_for_hos(bucket, key, filename) {
            //地址
            var url = 'http://' + serverURL + ':9080/hos/v1/object/content';
            //参数
            var formData = {
                bucket: bucket,
                key: key
            };
            console.log(url);

            let stream = fs.createWriteStream("././uploads/" + filename, {encoding: 'utf8'});

            request.get({url: url, formData: formData}, function (error, response, body) {
                if (error) {
                    console.log(error)
                }
            }).pipe(stream).on("close", function (err) {
                res.download("././uploads/" + filename, filename, function (err) {
                    if (err) {
                        // Handle error, but keep in mind the response may be partially-sent
                        // so check res.headersSent
                        console.log(err)
                    } else {

                        console.log("下载成功")
                        // decrement a download credit, etc.
                    }
                });
            });
        };
    })
};

exports.deleteFile = function (req, res) {

    var key = req.query.key;
    console.log("文件的key：" + key);
    request.get({url: 'http://' + serverURL + ':9080/hos/v1/bucket/list'}, function (error, response, body) {
        var body = eval("(" + body + ")");

        console.log("删除的时候bucket和Key的回显：" + body[0].bucketName + "-----------------" + key + "------" + sID);
        //转发删除请求


        var options = {
            url: 'http://' + serverURL + ':9080/hos/v1/object',

            formData: {
                bucket: body[0].bucketName,
                key: key
            }
        };

        request.del(options, function (err, response, body) {
            console.log(body);
            if (error) {
                console.log("删除发生错误：" + error)
            }

            res.json({succ: true});


        })

    });

}


exports.newFolder = function (req, res) {
    var folderName = req.query.key;
    //  bucket  key media file

    request.get({url: 'http://' + serverURL + ':9080/hos/v1/bucket/list'}, function (error, response, body) {

        var body = eval("(" + body + ")");

        upload_for_hos(body[0].bucketName, folderName + "/occupy.txt", "txt", "occupy.txt");


        function upload_for_hos(bucket, key, mediaType, filename) {
            var url = 'http://' + serverURL + ':9080/hos/v1/object';
            var formData = {
                // Pass a simple key-value pair
                bucket: bucket,
                // Pass data via Buffers
                key: key,
                // Pass data via Streams
                mediaType: mediaType,
                content: fs.createReadStream('././uploads/' + filename)

            };
            console.log(formData.content[0] + "/occupy.txt----------------------");
            var headers = {
                cookie: 'JSESSIONID=' + sID
            }
            console.log(headers.cookie);
            request.post({url: url, headers: headers, formData: formData}, function (error, response, body) {
                if (!error && response.statusCode == 200) {
                    console.log("新建文件夹成功");
                    res.json({succ: true});

                    return;
                }
                res.json({succ: false});
                console.log("错误类型 ：" + error)
                console.log("返回的数据 ：" + response.body)
                console.log("返回的数据 ：" + response.statusCode)

            })
            console.log('GOGOGOGOGOGO');
        }


    })

}


//登出功能
exports.logout = function (req, res) {
    request.get({url: 'http://' + serverURL + ':9080/logout'}, function (error, response, body) {
        if (JSON.parse(body).data == "success") {
            res.render("login")
        }
    })
}

//删除文件夹
exports.deleteFolder = function (req, res) {
    var rootKey = req.query.key;

    request.get({url: 'http://' + serverURL + ':9080/hos/v1/bucket/list'}, function (error, response, body) {

        var body = eval("(" + body + ")");
        // console.log(body);
        //console.log(body[0].bucketName  +"----"+key)


        //此处添加同步控制
        var p = 0;
        var q = 0;
        var lack = true;
        deleteAllFiles(body[0].bucketName, rootKey);


        function deleteAllFiles(bucket, key) {
            var opt = {
                url: 'http://' + serverURL + ':9080/hos/v1/object/list/dir',
                formData: {
                    bucket: bucket,
                    dir: key
                }

            }

            request.get(opt, function (error, response, body) {
                var fileObjectList = JSON.parse(body).objectList;
                console.log("当前所有文件的列表：" + fileObjectList);

                //递归删除文件夹
                //如果文件夹是空的,直接转入删除文件夹
                if (fileObjectList.length == 0) {
                    deleteAllFolders(bucket, rootKey);
                }
                //如果文件夹不是空的，先删除所有的文件
                for (var i = 0; i < fileObjectList.length; i++) {
                    if (parseInt(fileObjectList[i].length) != 0) {
                        p++;
                        console.log("递归删除文件过程中：" + fileObjectList[i].key);
                        console.log("递归删除文件过程中：" + fileObjectList.length);
                        console.log("递归删除文件过程中：" + fileObjectList[i].bucket);
                        var options = {
                            url: 'http://' + serverURL + ':9080/hos/v1/object',

                            formData: {
                                bucket: fileObjectList[i].bucket,
                                key: fileObjectList[i].key
                            }
                        };
                        request.del(options, function (err, response, body) {
                            if (error) {
                                console.log("删除发生错误：" + error);
                                return;
                            }
                            p--;
                            if (p == 0) //所有文件全部被删除，转入删除所有的文件夹
                            {
                                deleteAllFolders(bucket, rootKey);
                            }
                        })
                    } else  //如果删除的是文件夹，则进入文件夹
                    {
                        console.log("递归删除文件夹过程中：" + fileObjectList[i].key);
                        console.log("递归删除文件夹过程中：" + fileObjectList.length);
                        console.log("递归删除文件夹过程中：" + fileObjectList[i].bucket);
                        deleteAllFiles(bucket, fileObjectList[i].key);
                    }
                }
                // deleteAllFolders(body[0].bucketName,rootKey);


            })


        }

        function deleteAllFolders(bucket, key) {

            var deleteFolderFlag = true;

            var opt = {
                url: 'http://' + serverURL + ':9080/hos/v1/object/list/dir',
                formData: {
                    bucket: bucket,
                    dir: key
                }

            }

            console.log("删除文件的bucket和key：" + bucket + "------" + key)
            request.get(opt, function (error, response, body) {
                var fileObjectList = JSON.parse(body).objectList;
                console.log(fileObjectList);

                //递归删除文件夹
                for (var i = 0; i < fileObjectList.length; i++) {
                    q++;
                    deleteFolderFlag = false;
                    console.log("递归删除文件夹过程中2：" + fileObjectList[i].key);
                    console.log("递归删除文件夹过程中2：" + fileObjectList.length);
                    console.log("递归删除文件夹过程中2：" + fileObjectList[i].bucket);
                    deleteAllFolders(bucket, fileObjectList[i].key);

                }

                console.log("删除最终文件夹：" + bucket + "------" + key)
                if (deleteFolderFlag == true) {
                    var options = {
                        url: 'http://' + serverURL + ':9080/hos/v1/object',

                        formData: {
                            bucket: bucket,
                            key: key
                        }
                    };
                    request.del(options, function (err, response, body) {
                        if (error) {
                            console.log("删除发生错误：" + error)
                        } else {

                            console.log(body);
                            console.log("删除文件夹成功")
                            q--;
                            console.log("查看q的值:" + q)
                            if (q > 0) {

                                deleteAllFolders(bucket, rootKey);
                            } else if (lack == true) {
                                lack = false;
                                res.json({succ: true});
                            }

                        }

                    })

                }
            })


        }


    })
}
