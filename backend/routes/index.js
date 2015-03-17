var mongojs = require('mongojs');
var db      = mongojs('Gamify');
var request = require('request');

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

var cleanUser = function(user){
	return {
		id: user._id,
		name: user.name,
		email: user.email,
		level: user.total_lvl
	} 
}

var generateID = function(){
	//generate random number
	var randomDec = Math.random()
	return Math.round(randomDec * 10000000000)
}

var cleanDataWithTime = function(data, start, end){
	var dataWithRange = []
	var actData = []
	var dataArray = data.xyz
	var actArray = data.activity
	var tempString = ""
	var tempInt = 0
	var re = /(,)[^,]+(")/
	var re2 = /(,)[^,]+(,)/

	var dataArrayLength = dataArray.length
	var actArrayLength = actArray.length

	for (var i = 0; i < dataArrayLength; i++){
		var string = dataArray[i]
		tempString = string.match(re)
		tempInt = parseInt(tempString)
		if (tempInt >= start && tempInt <= end)
			dataWithRange.push(tempString)
	}

	for (var j = 0; j < actArrayLength; j++){
		var string = actArray[j]
		tempString = string.match(re2)
		tempInt = parseInt(tempString)
		if (tempInt >= start && tempInt <= end)
			actData.push(tempString)
	}

	return { 
		id: data._id, 
		xyz: dataWithRange,
		activity: actData
	}
}

var cleanData = function(data){
	return { 
		id: data._id, 
		xyz: data.xyz,
		activity: data.activity
	}
}

var cleanDataAct = function(data){
	return{
		id: data._id,
		activity: data.activity
	}
}

var cleanUserScores = function(data){
	return{
		id: data._id,
		totalScore: data.totalScore,
		sleepScore: data.sleepScore,
		foodScore: data.foodScore,
		exerciseScore: data.exerciseScore
	}
}


exports.storeXYZ = function(req, res) {
	printDate()
	console.log("\nstoreXYZ")
	console.log(req.body.userID)

	var user_id = req.body.userID
	var xyz_tuple = req.body.xyz
	//var activity = req.body.activity
	//console.log(xyz_tuple)

	if (!user_id || !xyz_tuple) return requestError(res, "missing userID and xyz_tuple")

	db.collection('data')
		.find(
			{_id: user_id},
			function(err, data){
				if (err) return serverError(res, err)

				if (data.length > 0) {
					db.collection('data').update(
						{_id: user_id},
						{$addToSet: {xyz: xyz_tuple}}
					)

					return res.send(data[0]._id)
				} 

				db.collection('data')
					.insert(
					{
						_id: user_id,
						xyz: [xyz_tuple],
						activity: [],
						confirmation: []
					},
					function(err, inserted){

						if (err) return serverError(res, err)

						res.send(inserted._id)
					}
				)
			}
		)
	}

exports.storeAct = function(req, res) {
	printDate()
	console.log("\nstoreAct")

	var user_id = req.body.userID
	var activity = req.body.activity

	if (!user_id || !activity) return requestError(res, "missing userID")

	db.collection('data')
		.find(
			{_id: user_id},
			function(err, data){
				if (err) return serverError(res, err)

				if (data.length > 0) {
					db.collection('data').update(
						{_id: user_id},
						{$addToSet: {activity: activity}}
					)
					return res.send(data[0]._id)
				} 

				db.collection('data')
					.insert(
					{
						_id: user_id,
						xyz: [],
						activity: [activity],
						confirmation: []
					},
					function(err, inserted){

						if (err) return serverError(res, err)

						res.send(inserted._id)
					}
				)
			}
		)
	}

exports.getAct = function(req, res){
	printDate()
	console.log("\ngetAct")
	console.log(req.body)

	var user_id = parseInt(req.body.userID)

	db.collection('data')
	.find(
		{_id:  user_id},
		function(err, data){
			if (err){
				console.log("error")
				return serverError(res, err)
			} 

			if (data.length > 0){
				console.log(cleanDataAct(data[0]))
				res.send(cleanDataAct(data[0]))
			}
				
		}
	)

}

exports.getData = function(req, res){
	printDate()
	console.log("\ngetData")
	console.log(req.body)

	var user_id = parseInt(req.body.userId)
	var start   = req.body.startTime
	var end     = req.body.endTime

	if (!start && !end){
		console.log("No Start and No End")
		db.collection('data')
		.find(
			{_id:  user_id},
			function(err, data){
				if (err){
					console.log("error")
					return serverError(res, err)
				} 

				if (data.length > 0){
					console.log(cleanData(data[0]))
					res.send(cleanData(data[0]))
				}
					
			}
		)

	}

	else if(!start){
		var start = 0;
		console.log("No start")

		db.collection('data')
		.find(
			{_id:  user_id},
			function(err, data){
				if (err){
					console.log("error")
					return serverError(res, err)
				} 

				if (data.length > 0){
					console.log(cleanData(data[0], start, end))
					res.send(cleanData(data[0], start, end))
				}
					
			}
		)

	}

	else if(!end){
		var end = 1000000000000000000000;
		console.log("No end")

		db.collection('data')
		.find(
			{_id:  user_id},
			function(err, data){
				if (err){
					console.log("error")
					return serverError(res, err)
				} 

				if (data.length > 0){
					console.log(cleanData(data[0], start, end))
					res.send(cleanData(data[0], start, end))
				}
					
			}
		)

	}
	
}

exports.storeTestConfirm = function(req, res){
	printDate()
	console.log("storeTestConfirm")
	console.log(req.body)
	
	var user_id = parseInt(req.body.userID)
	var confirm = req.body.confirm
	var time    = req.body.timestamp

	if (!user_id || !confirm) return requestError(res, "missing userID or confirmation")

	db.collection('data')
		.find(
			{_id: user_id},
			function(err, data){
				if (err){
					return serverError(res, err)
				res.send("failure")
			}


				if (data.length > 0) {
					db.collection('data').update(
						{_id: user_id},
						{$addToSet: {confirmation: confirm}}
					)
					console.log("success")
					return res.send("success")
				} 

				db.collection('data')
					.insert(
					{
						_id: user_id,
						xyz: [],
						activity: [],
						confirmation: [confirm]
					},
					function(err, inserted){

						if (err) return serverError(res, err)
						console.log("success")
						res.send("success")
					}
				)
			}
		)
	}



exports.storeScore = function(req, res){
	printDate()
	console.log("storeScore")

	var user_id         = parseInt(req.body.userID)
	var total_vit       = req.body.vitality

	if (!user_id) return requestError(res, "missing userID")

	db.collection('userScores')
		.find(
			{_id: user_id},
			function(err, data){
				if (err){
					return serverError(res, err)
					res.send("failure")
				}

				if (data.length > 0) {
					db.collection('userScores').update(
						{_id: user_id},
						{$set: {totalVit: total_vit}}
					)
					return res.send(data[0]._id)
				}

				db.collection('userScores')
					.insert(
					{
						_id: user_id,
						totalVit: total_vit
					},
					function(err, inserted){

						if (err) return serverError(res, err)

						res.send(inserted._id)
					}
				)
			}
		)
	}


exports.storeFood = function(req, res){
	printDate()
	console.log("storeFood")

	var user_id         = parseInt(req.body.userID)
	var food            = req.body.food	
	var servings        = req.body.servings

	if (!user_id || !food) return requestError(res, "missing userID or food")

	db.collection('food')
		.find(
			{_id: user_id},
			function(err, data){
				if (err){
					return serverError(res, err)
					res.send("failure")
				}

				if (data.length > 0) {
					db.collection('data').update(
						{_id: user_id},
						{$addToset: {food_list: food}}
					)
					return res.send(data[0]._id)
				}

				db.collection('data')
					.insert(
					{
						_id: user_id,
						food_list: [food]
					},
					function(err, inserted){

						if (err) return serverError(res, err)

						res.send(inserted._id)
					}
				)

		}
	)
}

exports.storeActTime = function(req, res){
	printDate()
	console.log("storeActTime")

	var user_id         = parseInt(req.body.userID)
	var minWalked       = req.body.minWalked
	var minBiked        = req.body.minBiked
	var minRan			= req.body.minRan
}

exports.storeRooms = function(req, res){
	printDate()
	console.log("storeRooms")
	console.log(req.body)

	var user_id         = parseInt(req.body.userID)
	var room            = req.body.rooms

	if (!user_id) return requestError(res, "missing userID")

	db.collection('userRooms')
		.find(
			{_id: user_id},
			function(err, data){
				if (err){
					return serverError(res, err)
				res.send("failure")
				}

				if (data.length > 0) {
					db.collection('userRooms').update(
						{_id: user_id},
						{$set: {rooms: room}}
					)
					return res.send(data[0]._id)
				}

				db.collection('userRooms')
					.insert(
					{
						_id: user_id,
						rooms: room
					},
					function(err, inserted){

						if (err) return serverError(res, err)

						res.send(inserted._id)
					}
				)

	
	}
	)
	}

exports.getScore = function(req, res){
	printDate()
	console.log("getScore")

	var user_id         = parseInt(req.body.userID)

	db.collection('userScores')
	.find(
		{_id:  user_id},
		function(err, data){
			if (err){
				console.log("error")
				return serverError(res, err)
			} 

			if (data.length > 0){
				console.log(cleanUserScores(data[0]))
				res.send(cleanUserScores(data[0]))
			}
				
		}
	)

}

exports.getFood = function(req, res){
	printDate()
	console.log("getFood")

	var user_id         = parseInt(req.body.userID)

	db.collection('food')
	.find(
		{_id:  user_id},
		function(err, data){
			if (err){
				console.log("error")
				return serverError(res, err)
			} 

			if (data.length > 0){
				console.log(data[0].food)
				res.send(data[0].food)
			}
				
		}
	)

}

exports.getActTime = function(req, res){
	printDate()
	console.log("getActTime")

	var user_id         = parseInt(req.body.userID)

}

exports.getRooms = function(req, res){
	printDate()
	console.log("getRooms")

	var user_id         = parseInt(req.body.userID)

	db.collection('rooms')
	.find(
		{_id:  user_id},
		function(err, data){
			if (err){
				console.log("error")
				return serverError(res, err)
			} 

			if (data.length > 0){
				console.log(data[0].rooms)
				res.send(data[0].rooms)
			}
				
		}
	)
}






