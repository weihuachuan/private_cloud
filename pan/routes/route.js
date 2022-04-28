var User = require('../app/controller/user')
var express = require('express');
var router = express.Router();


router.use(function (req, res, next) {
    var _userSession = req.session.user
    if (_userSession) {
        app.locals.user = _userSession
    }
    return next()
})


var multer = require('multer')

var uploadFolder = './upload';
var storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, uploadFolder);    // 保存的路径，备注：需要自己创建
    }
});
var upload = multer({storage: storage, limits: {fieldSize: 1024 * 1024 * 1024 * 2}})


//注册
router.get('/register', User.register)
router.post('/registerPost', User.registerPost)

//登录
router.get('/login', User.login)
router.post('/loginPost', User.loginPost)
//登出用户
router.get('/logout', User.logout)

//主界面
router.get('/mainUI', User.main)
router.get('/queryDir', User.queryDir)

//上传
router.get('/uploadPage', User.uploadPage);
router.post('/uploadPage1', multer({dest: "./uploads"}).array("content"), User.uploadPage1)
//新建文件夹
router.get('/newFolder', User.newFolder);
//删除文件夹
router.get('/deleteFolder', User.deleteFolder);
//文件下载
router.get('/download', User.download);
//文件删除

router.get('/deleteFile', User.deleteFile);


module.exports = router;