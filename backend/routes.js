//routes.js
//grab the models
var User = require('./models/user');

module.exports = function(app) {

	//server routes ===========================
	//handle API calls
	//authentication routes

	//Sample API Route
	app.get('/api/test', function(req, res) {

		User.find(function(err, users) {

			if(err) res.send(err);

			//Return Users in JSON format
			res.json(users); 
		});
	});
}

/**
	app.post('/api/users', function(req, res) {
		printData()
		console.log("\ngetUserId")
		console.log(req.body)

		var email = req.body.email
		var name = req.body.name
		if (!email || !name) return requestError(res, "missing email or name")

		db.collection('users')
			.find(
				{email: email},
				function(err, users) {
					if (err) return serverError(res, err)

					if (users.length > 0) return res.send(players[0]._id)

					db.collection('users')
						.insert(
						{
							user_name: name,
							user_email: email,
							user_total_lvl: 0,
						},
						function(err, inserted){
							if (err) return serverError(res, err)

							res.send(inserted._id)
						})
				}
				)

	})
	**/