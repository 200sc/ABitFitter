var mongojs = require('mongojs')
var db      = mongojs('trap')
var request = require('request')


var serverError = function(res, err) {
  console.log(err)
  res.send(500)
}

var requestError = function(res, err) {
  console.log(err)
  res.send(400, {error: err})
}

var printDate = function() {
  console.log("\n")
  console.log(new Date().getTime())
}

// Convert a user returned by the database to a json object for sending to app
var cleaned_user = function(user) {
  return {
    id: user._id,
    name: user.name,
    email: user.email,
    level: user.total_lvl
  }
}

exports.getUserId = function(req, res) {
  printDate()
  console.log("\ngetUserId")
  console.log(req.body)

  var email = req.body.email
  var name  = req.body.name
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
              user_email: email ,
              user_total_lvl: 0,
            },
            function(err, inserted) {
              if (err) return serverError(res, err)

              res.send(inserted._id)
            }
          )
      }
    )
}

exports.getUserRooms = function(req, res) {
  printDate()
  console.log("\ngetUserRooms")
  console.log(req.body)

  var userId = req.body.userId
  if (!email || !name) return requestError(res, "missing user id")

    db.collection('rooms').find(
    {user_id: id},
    function(err, rooms) {
      if (err) return serverError(res, err)

      if (rooms.length > 0) return res.send(rooms[0])
    }

exports.getUserItems = function(req, res) {
  //to do
}

exports.getUserInfo = function(req, res) {
  //to do
}

