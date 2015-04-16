var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var rp = require('request-promise');

app.use(bodyParser.json());


function multiply(m, n) {
	var nx = m.nx;

	var result = {};
	result.nx = m.nx;
	result.data = new Array(m.nx * m.nx);

	for (var i = 0; i < nx; i++) {
		for (var j = 0; j < nx; j++) {

			var sum = 0;
			for (var k = 0; k < nx; k++) {
				sum += m.data[i * nx + j] * n.data[k * nx + j];
			}

			result.data[i * nx + j] = sum;
		}
	}

	return result;
}

app.post('/matrix/power', function (req, res) {

	var m = req.body;

	if (m.right == 1) {

		res.send(m.left);
	} else {

		var reqData = {}
		reqData.right = m.right - 1;
		reqData.left = m.left;

		var options = {
			uri: 'http://localhost:8080/matrix/power',
			method: 'POST',
			json: reqData
		};

		rp(options)
			.then(function (response) {

				res.send(multiply(m.left, response));
			});
	}
});
var server = app.listen(8080, function () {

	var host = server.address().address;
	var port = server.address().port;

	console.log('Example app listening at http://%s:%s', host, port);

});