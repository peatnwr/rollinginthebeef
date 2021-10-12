let express = require('express');
let bodyParser = require('body-parser');
let mysql = require('mysql');
let app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

let dbConn = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'rollinginthebeef'
});

dbConn.connect();

app.get('/allproduct/', function(req, res) {
    dbConn.query('SELECT `product`.`product_id`, `product`.`product_name`, `product`.`product_price`, `product`.`product_detail`, `product`.`product_img`, `product`.`category_id`, `category`.`category_name` FROM product, category WHERE `product`.`category_id` = `category`.`category_id`', function(error, results, fields) {
        if(error) throw error;
        return res.send(results);
    });
});

app.get('/order/', function(req, res) {
    dbConn.query('SELECT `user`.`user_id`, `user`.`user_username`, `user`.`user_name`, `order`.`order_id`, `order`.`order_date`, `order`.`order_total`, `order`.`order_status` FROM `user`, `order` WHERE `user`.`user_id` = `order`.`user_id` AND `order`.`order_status` = 2', function(error, results, fields) {
        if(error) throw error;
        return res.send(results);
    });
})

app.get('/orderdetail/:order_id/:user_id', function(req, res) {
    let orderID = req.params.order_id
    let userID = req.params.user_id

    if(!orderID || !userID){
        res.status(400).send({ error: true, message: "Please provide order id and user id" })
    }else{
        dbConn.query('SELECT `order`.`order_id`, `order`.`order_date`, `user`.`user_name`, `user`.`user_address`, `orderdetail`.`orderdetail_qty`, `product`.`product_name`, `orderdetail`.`orderdetail_price`, `order`.`order_total`, `order`.`receipt_img` FROM `order`, `user`, `product`, `orderdetail` WHERE `order`.`order_id` = ? AND `orderdetail`.`order_id` = ? AND `user`.`user_id` = ? AND `orderdetail`.`product_id` = `product`.`product_id`;', [orderID, orderID, userID], function(error, results, fields) {
            if(error) throw error;
            return res.send(results)
        })
    }
})

app.patch('/confirmpayment/:order_id', function(req, res) {
    let orderID = req.params.order_id

    if(!orderID){
        res.status(400).send({ error: true, message: "Please provide order id" })
    } else {
        dbConn.query('UPDATE `order` SET `order`.`order_status` = 3, `order`.`order_tracking` = 1 WHERE `order`.`order_id` = ?', orderID, function(error, results, fields) {
            if(error) throw error;
            return res.send(results);
        });
    }
});

app.get('/deliverystatus/', function(req, res) {
    dbConn.query('SELECT `user`.`user_id`, `user`.`user_name`, `user`.`user_address`, `order`.`order_id`, `order`.`order_date`, `order`.`order_total`, `order`.`order_status` FROM `user`, `order` WHERE `user`.`user_id` = `order`.`user_id` AND `order`.`order_status` = 3 AND `order`.`order_tracking` = 1', function(error, results, fields) {
        if(error) throw error;
        return res.send(results);
    });
});

app.post('/login/', function(req, res) {
    let data = req.body // GET POST params

    let username = data.user_username;
    let password = data.user_password;

    dbConn.query('SELECT * FROM user WHERE user_username = ?', [username], function(error, results, fields) {
        if(error) throw error;

        if(results && results.length){

            let passwordDB = results[0].user_password;

            if(password == passwordDB){
                return res.send(results[0]);
            } else {
                res.status(400).send({ error: true, message : "Username or Password does not match." });
            }
        } else {
            res.status(400).send({ error : true, message: "User not exist." });
        }
    });
});

app.patch('/changepassword/', function(req, res) {
    let data = req.body //GET POST params

    let username_forgetpw = data.user_username;
    let password_forgetpw = data.user_password;
    let cfpassword_forgetpw = data.user_cfpassword;
    let cfPassword = false;

    if(password_forgetpw == cfpassword_forgetpw){
        cfPassword = true;
    }

    dbConn.query('SELECT * FROM user WHERE user_username = ?', [username_forgetpw], function(error, results, fields) {
        dbConn.on('error', function(error) {
            console.log('selectForgetPassword Alert!!', error);
            res.send("changepasswordQuery Alert!!", error);
        });
        if(results && results.length){
            if(cfPassword == true){
                dbConn.query('UPDATE `user` SET `user_password` = ? WHERE `user_username` = ?', [password_forgetpw, username_forgetpw], function(error, results, fields) {
                    dbConn.on('error', function(error) {
                        console.log('updateForgetPassword Alert!!', error)
                    });
                    res.send(results);
                });
            } else {
                res.send("Password doest not match!!!");
            }
        } else {
            res.send("User not exist!!!");
        }
    });
});

app.post('/register/', function(req, res) {
    let data = req.body // GET POST params

    let username_reg = data.user_username;
    let password_reg = data.user_password;
    let tel = data.user_tel;
    let address = data.user_address;
    let email = data.user_email;
    let name = data.user_name;

    dbConn.query('SELECT * FROM user WHERE user_username = ? AND user_email = ?', [username_reg, email], function(error, results, fields) {
        dbConn.on('error', function(error) {
            console.log('MySQL Error!!!', error);
        });
        if(results && results.length){
            res.send('User already exist!!!');
        } else {
            dbConn.query('INSERT INTO `user`(`user_username`, `user_password`, `user_tel`, `user_address`, `user_email`, `user_name`) VALUES (?,?,?,?,?,?)', [username_reg, password_reg, tel, address, email, name], function(error, results, fields) {
                dbConn.on('error', function(error) {
                    console.log('MySQL Error!!!', error);
                });
                res.send(results);
            });
        }
    });
});

app.listen(3000, function() {
    console.log('Node app is running on port 3000');
});

module.exports = app;