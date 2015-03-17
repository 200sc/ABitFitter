var mongojs       = require('mongojs')
var db            = mongojs('Gamify')
var request       = require('request')
var mongoose      = require('mongoose')
var localStrategy = require('passport-local').Strategy
var User          = require('../config/user.js')

var serverError = function(res, err){
	console.log(err);
	res.send(500);
}

var requestError = function(res, err){
	console.log(err);
	res.send(400, {error: err});
}

var printDate = function(){
	console.log("\n");
	console.log(new Date().getTime());
}

var generateID = function(){
	//generate random number
	var randomDec = Math.random()
	return Math.round(randomDec * 10000000000)
}

var cleanUser = function(user){
	return {
		id: (user._id).toString(),
		name: user.username,
		status: "true"
	} 
}

var cleanFAILURE = function(){
	return{
		status: "false"
	}
}

exports.localSignIn = function(passport){
	passport.serializeUser(function(user, done) {
		done(null, user.id)
	})

	passport.deserializeUser(function(id, done) {
		User.findById(id, function(err, user) {
			done(err, user)
		})
	})

	passport.use('local-signup', new LocalStrategy({

		usernameField: 'username',
		passwordField: 'password',
		emailField:    'email',
		passReqToCallback: true
	},
	function(req, username, password, email, done){

		process.nextTick(function() {

			User.findOne({'local.email' : email}, function(err, user) {

				if(err)
					return done(err);

				if(user){
					return done(null, false, req.flash('signupMessage', 'That email is already taken.'))
				} else {

					var newUser           = new User()

					newUser.local.username = userName
					newUser.local.email    = email
					newUser.local.password = newUser.generateHash(password)

					newUser.save(function(err) {
						if (err)
							throw err;
						return done(null, newUser)
					})
				}
			})
		})


	}))
}

exports.signIn = function(req, res){
	printDate()
	console.log("\nsignIn")
	console.log(req.body)

	var user_name  = req.body.username
	var pass   = req.body.password

	if (!user_name || !pass) return requestError(res, "Missing userName or Password")

	db.collection('users')
		.find(
			{username: user_name},
			function(err, data){
				if (err) return serverError(res, err)

				if (data.length > 0){
					if (data[0].password == pass){
						console.log("success")
						console.log(cleanUser(data[0]))
						res.send(cleanUser(data[0]))

					}

					else{
						console.log("failure")
						console.log(cleanFAILURE())
						res.send(cleanFAILURE())

					}
				}
			}
			)	
}

exports.signUp = function(req, res){
	printDate()
	console.log("\nsignUp")
	console.log(req.body)

	var user_name = req.body.username
	var pass      = req.body.password
	var email     = req.body.email;
	var id        = generateID()

	db.collection('users')
		.find(
			{username: user_name},
			function(err, data){
				if (err) return serverError(res, err)

				if (data.length > 0){
					res.send("false")
					return requestError(res, "Username is already taken")
				}

				db.collection('users')
					.insert(
					{
						username: user_name,
						password: pass,
						email: email,
						_id: id
					},
					function(err, inserted){
						if (err) return serverError(res, err)
						res.send("true")
					}
				)
			}
		)
}

