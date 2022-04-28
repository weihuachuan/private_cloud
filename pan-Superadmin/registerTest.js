 	  var request = require('request');
	  request = request.defaults({jar: true})


 	  var usr = 'xyx1';
      var psd = '123456';
      var role1 = 'USER';
      var details = 'ajdsddd';

      console.log(usr+"  "+psd+"  "+role1+"  "+details);

      var options1 = {
        url: 'http://192.168.58.129:9080/loginPost',
        form: {
            username:"Superadmin",
            password:"superadmin"
        }  
      };
      request.post(options1, function(error, response, body) {
 		console.log("我是发往"+options1.url+"---"+JSON.stringify(response));
        console.log("我是发往"+options1.url+"---"+body);
        console.log("我是发往"+options1.url+"---"+error);
        console.log(usr+"  "+psd+"  "+role1+"  "+details);
        var options = {
        url: 'http://192.168.58.129:9080/hos/v1/sys/user',
        form: {
            userName:usr,
            password:psd,
            role:role1,
            detail:details
            }  
        };
         console.log( JSON.stringify(options)   );
        request.post(options, function(error, response, body) {
          console.log("我是发往"+options.url+"---"+JSON.stringify(response));
          console.log("我是发往"+options.url+"---"+body);
          console.log("我是发往"+options.url+"---"+error);
        
        })
      })