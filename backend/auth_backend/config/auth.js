//config/auth.js

//expose our config directly to out application using module.exports
module.exports = {

	'facebookAuth'{
		'clientID'         :     '1412780445688471', //appId
		'clientSecret'     :     '33a2a53768a6dbec6a13138c206eff8c', //app secret
		'callbackURL'      :     'http://localhost:8080/auth/facebook/callback'
	}
};