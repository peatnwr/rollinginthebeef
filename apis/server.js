let express = require('express');
let app = express();
let bodyParser = require('body-parser');
let mysql = require('mysql');
const e = require('express');
const { json } =  require('body-parser')

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

app.get('/allcategory/', function(req, res){
    dbConn.query('SELECT * FROM category', function(error, results, fields){
        if (error) throw error;
        return res.send(results)
    })
})

app.get('/userpermission/', function(req, res) {
    dbConn.query('SELECT * FROM `user` WHERE `user_type` != 0 ', function(error, results, fields) {
        if(error) throw error;
        return res.send(results);
    });
});

app.delete('/userpermission/:user_id', function(req, res) {
    let userId = req.params.user_id

    if(!userId){
        res.status(400).send({ error: true, message: "Please provide user id" })
    } else {
        dbConn.query('DELETE FROM `user` WHERE `user_id` = ?', userId, function(error, results, fields) {
            if(error) throw error;
            res.send(results);
        });
    }
});

app.get('/customeraccounts/', function(req, res) {
    dbConn.query('SELECT * FROM `user` WHERE `user_type` = 0', function(error, results, fields) {
        if(error) throw error;
        return res.send(results);
    });
});

app.delete('/customeraccounts/:user_id', function(req, res) {
    let userId = req.params.user_id

    if(!userId){
        res.status(400).send({ error: true, message: "Please provide user id" })
    } else {
        dbConn.query('DELETE FROM `user` WHERE `user_id` = ?', userId, function(error, results, fields) {
            if(error) throw error;
            res.send(results);
        });
    }
});

app.get('/orderhistoryadmin/', function(req, res) {
    dbConn.query('SELECT `order`.`order_id`, `order`.`order_date`, `order`.`order_total`, `user`.`user_id`, `user`.`user_name` FROM `order`, `user` WHERE `user`.`user_id` = `order`.`user_id` AND `order`.`order_status` = 4 AND `order`.`order_tracking` = 3', function(error, results, fields) {
        if(error) throw error;
        return res.send(results)
    });
})

app.get('/orderhistoryrider/', function(req, res) {
    dbConn.query('SELECT `order`.`order_id`, `order`.`order_date`, `order`.`order_total`, `user`.`user_id`, `user`.`user_name` FROM `order`, `user` WHERE `user`.`user_id` = `order`.`user_id` AND `order`.`order_status` = 4 AND `order`.`order_tracking` = 3', function(error, results, fields) {
        if(error) throw error;
        return res.send(results)
    });
})

app.get('/allproduct/', function(req, res) {
    dbConn.query('SELECT `product`.`product_id`, `product`.`product_name`, `product`.`product_price`, `product`.`product_detail`, `product`.`product_img`, `product`.`product_qty` ,`product`.`category_id`, `category`.`category_name` FROM product, category WHERE `product`.`category_id` = `category`.`category_id`', function(error, results, fields) {
        if(error) throw error;
        return res.send(results);
    });
});

app.get('/editproductadmin/:product_name', function(req,res){
    var product_name = req.params.product_name
    dbConn.query('SELECT product_name, product_price, product_detail, product_img, product_qty, category_id FROM product WHERE product_name = ?', [product_name], function(error, results, fields){
        if (results[0]){
            return res.send(results[0])
        }else{
            throw error;
        }
    })
})

app.get('/order/', function(req, res) {
    dbConn.query('SELECT `user`.`user_id`, `user`.`user_username`, `user`.`user_name`, `order`.`order_id`, `order`.`order_date`, `order`.`order_total`, `order`.`order_status` FROM `user`, `order` WHERE `user`.`user_id` = `order`.`user_id` AND `order`.`order_status` = 2', function(error, results, fields) {
        if(error) throw error;
        return res.send(results);
    });
})

app.get('/addproductpage/', function(req, res) {
    dbConn.query('SELECT `product`.`product_name`, `product`.`product_price`, `product`.`product_img`, `product`.`product_qty`, `category`.`category_name` FROM product, category WHERE `product`.`category_id` = `category`.`category_id`', function(error, results, fields) {
        if(error) throw error;
        return res.send(results)
    });
});

app.get('/orderdetail/:order_id/:user_id', function(req, res) {
    let orderID = req.params.order_id;
    let userID = req.params.user_id;

    if(!orderID && !userID){
        res.status(400).send({ error: true, message: "Please provide order id and user id" });
    }else{
        dbConn.query('SELECT `order`.`order_id`, `order`.`order_date`, `user`.`user_name`, `user`.`user_address`, `orderdetail`.`orderdetail_qty`, `product`.`product_name`, `orderdetail`.`orderdetail_price`, `order`.`order_total`, `order`.`receipt_img` FROM `order`, `user`, `product`, `orderdetail` WHERE `order`.`order_id` = ? AND `orderdetail`.`order_id` = ? AND `user`.`user_id` = ? AND `orderdetail`.`product_id` = `product`.`product_id`;', [orderID, orderID, userID], function(error, results, fields) {
            if(error) throw error;
            return res.send(results);
        });
    }
});

app.get('/orderhistorydetail/:order_id/:user_id', function(req, res) {
    let orderId = req.params.order_id
    let userId = req.params.user_id

    if(!orderId && !userId){
        res.status(400).send({ error: true, message: "Please provide order id and user id" });
    }else{
        dbConn.query('SELECT `order`.`order_id`, `order`.`order_date`, `user`.`user_name`, `user`.`user_address`, `orderdetail`.`orderdetail_qty`, `product`.`product_name`, `orderdetail`.`orderdetail_price`, `order`.`order_total`, `order`.`order_received_time`, `order`.`receipt_img` FROM `order`, `user`, `product`, `orderdetail` WHERE `order`.`order_id` = ? AND `orderdetail`.`order_id` = ? AND `user`.`user_id` = ? AND `orderdetail`.`product_id` = `product`.`product_id`;', [orderId, orderId, userId], function(error, results, fields) {
            if(error) throw error;
            return res.send(results);
        });
    }
});

app.get('/deliveryinfo/:order_id/:user_id', function(req, res) {
    let orderId = req.params.order_id
    let userId = req.params.user_id

    if(!orderId && !userId){
        res.status(400).send({ error: true, message: "Please provide order id and user id" });
    } else {
        dbConn.query('SELECT `order`.`order_id`, `order`.`order_date`, `user`.`user_name`, `user`.`user_address`, `orderdetail`.`orderdetail_qty`, `product`.`product_name`, `orderdetail`.`orderdetail_price`, `order`.`order_total`, `order`.`order_status`, `order`.`order_tracking` FROM `order`, `user`, `orderdetail`, `product` WHERE `order`.`order_id` = ? AND `orderdetail`.`order_id` = ? AND `user`.`user_id` = ? AND `orderdetail`.`product_id` = `product`.`product_id`;', [orderId, orderId, userId], function(error, results, fields) {
            if(error) throw error;
            return res.send(results);
        })
    }
});

app.post('/createorder/', function(req,res){
    var username = req.body.username
    var insert = "INSERT INTO `order`(order_status, user_id) VALUES (0, (SELECT user_id FROM user WHERE user_username = '"+ username +"'))"
    var qry = "SELECT order.order_id FROM `order` WHERE user_id = (SELECT user.user_id FROM user WHERE user.user_username = '"+ username +"') AND order_status = 0"
    dbConn.query( qry, function(error, results, fields){
        if (results.length == 0 || results === undefined){
            dbConn.query( insert, function(error,results, fields){
                if(results === undefined || results.length == 0){
                    return res.json('Already have order')
                } else{
                    return res.send(results);
                }
            })
        } else {
            return res.json('Already have order')
        }
    })
});

app.post('/addorder/', function(req,res){
    var product = req.body.product_id
    var username = req.body.username 
    var price = req.body.product_price
    var qry = "INSERT INTO orderdetail VALUES ((SELECT DISTINCT order.order_id FROM `order` WHERE order.user_id = (SELECT user_id FROM user WHERE user_username = '" + username +  "') AND order.order_status = 0), " + product +",1, " + price +")";
    var select = "SELECT DISTINCT orderdetail.product_id FROM orderdetail, `order` WHERE orderdetail.product_id = " + product +" AND orderdetail.order_id = (SELECT order.order_id FROM `order` WHERE order.user_id = (SELECT user.user_id FROM user WHERE user.user_username = '"+ username +"') AND order.order_status = 0)"
    var update = "UPDATE orderdetail SET orderdetail.orderdetail_qty = (orderdetail.orderdetail_qty + 1) WHERE orderdetail.order_id = (SELECT order_id FROM `order` WHERE order_status = 0 AND user_id = (SELECT user_id FROM user WHERE user_username = '" + username + "')) AND orderdetail.product_id = " + product
    dbConn.query(select, function(error, results, fields){
        if (results === undefined || results.length == 0){
            dbConn.query( qry , function(error,result, fields){
                if(results){
                    return res.json('Add product successfully');
                } else{
                    return res.json("Can't add product");
                }
            })
        }else{
            dbConn.query(update, function(error, uresults, fields){
                if (uresults){
                    return res.json('Update successfully')
                }
            })
        }
    })
});

app.post('/cartproduct/', function(req,res){
    var order_id = req.body.order_id
    var qry = "SELECT DISTINCT orderdetail.orderdetail_qty,category.category_name,product.product_name,product.product_price,product.product_img, product.product_price*orderdetail.orderdetail_qty AS total FROM orderdetail,product,category,`order` WHERE `order`.`order_id` = " + order_id + " AND orderdetail.order_id = " + order_id + " AND `order`.`order_status` = 0 AND orderdetail.product_id = product.product_id AND product.category_id = category.category_id"
    dbConn.query(qry, function(error,results, fields){
        if(results){
            return res.send(results);
        }else{
            return res.json('No product in cart')
        }
        
    });
});

app.post('/addqty/', function(req,res) {
    var product = req.body.product_name
    var username = req.body.username
    var add = "UPDATE orderdetail SET orderdetail.orderdetail_qty = (orderdetail.orderdetail_qty + 1) WHERE orderdetail.order_id = (SELECT order.order_id FROM `order` WHERE order.user_id = (SELECT user.user_id FROM user WHERE user.user_username = '" + username + "') AND order.order_status = 0) AND orderdetail.product_id = (SELECT product_id FROM product WHERE product_name = '" +  product + "')" 
    dbConn.query(add, function(error, results, fields){
            if (results){
                return res.send(results)
            }else{
                return res.status(400).send({error: true, message: "Can't Add Product"})
            }
    })
})

app.post('/rmvqty/', function(req,res) {
    var product = req.body.product_name
    var username = req.body.username
    var add = "UPDATE orderdetail SET orderdetail.orderdetail_qty = (orderdetail.orderdetail_qty - 1) WHERE orderdetail.order_id = (SELECT order.order_id FROM `order` WHERE order.user_id = (SELECT user.user_id FROM user WHERE user.user_username = '" + username + "') AND order.order_status = 0) AND orderdetail.product_id = (SELECT product_id FROM product WHERE product_name = '" +  product + "')" 
    dbConn.query(add, function(error, results, fields){
        if (results){
            return res.send(results)
        }else{
            return res.status(400).send({error: true, message: "Can't Remove Product"})
        }
    })
})

app.get('/getuserdata/:username', function(req, res){
    var username = req.params.username
    if (!username){
        return res.status(400).send({ error:true, message: 'Please provide username'});
    }
    dbConn.query("SELECT user_id, user_tel, user_address, user_email, user_name FROM user WHERE user_username = ?", [username], function(err, results, fields){
        if (results[0]){
            return res.send(results[0])
        }else{
            return res.status(400).send({ error: true, message: 'User Not Found !' })
        }
    })
})

app.get('/getorderid/:username', function(req, res) {
    let username = req.params.username
    if (!username){
        return res.status(400).send({ error:true, message: 'Please provide username'});
    }
    var getId = "SELECT `order`.order_id FROM `order` WHERE `order`.`user_id` = (SELECT user.user_id FROM user WHERE user.user_username =  '" + username + "') AND `order`.`order_status` = 0 ORDER BY `order`.`order_id` DESC LIMIT 1"
    dbConn.query(getId, function(error, results, fields){
        if (error) throw error;
        if (results[0]){
            return res.send(results[0])
        } else {
            return res.status(400).send({ error: true, message: 'Order ID Not Found !' })
        }
    })
})

app.get('/getorderhistory/:username', function(req, res){
    var username = req.params.username
    if (!username){
        return res.status(400).send({ error:true, message: 'Please provide user ID'});
    }
    dbConn.query("SELECT * FROM `order` WHERE `order`.`user_id` = (SELECT user_id FROM user WHERE user_username = ?) AND `order`.`order_status` IN (4,5) ORDER BY `order`.`order_id` DESC", [username], function(error, results, fields){
        if (results){
            return res.send(results)
        }else{
            return res.status(400).send({ error: true, message: 'Order ID Not Found !' })
        }
    })
})

app.post('/removecartproduct/', function(req,res){
    var username = req.body.username
    var product_name = req.body.product_name
    if (!username){
        return res.status(400).send({error: true, message: "No order found"})
    }
    dbConn.query("DELETE FROM orderdetail WHERE order_id = (SELECT order.order_id FROM `order` WHERE order.user_id = (SELECT user.user_id FROM user WHERE user.user_username = ?) AND order.order_status = 0) AND product_id = (SELECT product_id FROM product WHERE product_name = ?)", [username, product_name], function(err, results, fields){
        if (results){
            return res.send(results)
        }else{
            return res.status(400).send({error: true, message: "Delete product failed !!!"})
        }
    })
})

app.get('/receiptimage/:order_id', function(req,res){
    var order_id = req.params.order_id
    if(!order_id){
        return res.status(400).send({ error:true, message: 'Please provide Order ID'});
    }
    dbConn.query("SELECT receipt_img from `order` WHERE order_id = ?", [order_id], function(err, results, fields){
        if (results[0]){
            return res.send(results[0])
        }else{
            return res.status(400).send({ error: true, message: 'Receipt image Not Found !' })
        }
    })
})

app.get('/activeorder/:username', function(req, res) {
    var username = req.params.username
    if (!username){
        return res.status(400).send({error:true, message: 'No user found'})
    }
    dbConn.query("SELECT * FROM `order` WHERE `order`.`user_id` = (SELECT user_id FROM user WHERE user_username = ?) AND `order`.`order_status` IN (1,2,3)  ", [username], function(error, results, fields){
        if (results[0]){
            return res.send(results[0])
        }else{
            return res.status(400).send({ error: true, message: 'Order ID Not Found !' })
        }
    })
})

app.get('/getnewproduct/', function(req, res){
    dbConn.query("'SELECT product.product_id,product.product_name,product.product_price,product.product_detail,product.product_img, product.category_id,category.category_name FROM product,category WHERE product.category_id = category.category_id AND product.product_id > 53'", function(err, results, fields) {
        if (results){
            return res.send(results)
        }else{
            return res.status(400).send({error: true, message: "No order found"})
        }
    })
})

app.put('/cancelorder/:order_id', function(req, res){
    var order_id = req.params.order_id
    if (!order_id){
        return res.status(400).send({error: true, message: 'No Order Found'})
    }
    dbConn.query("UPDATE `order` SET order_status = 5 WHERE order_id = ?", [order_id], function(err, results, fields){
        if (results){
            return res.send(results)
        }else{
            return res.status(400).send({ error: true, message: "Cancel order failed" })
        }
    })
})

app.post('/confirmorder/', function(req,res) {
    var username = req.body.username
    var date = req.body.order_date
    var time = req.body.order_time 
    var total = req.body.order_total
    var confirm = "UPDATE `order` SET `order`.`order_status` = 1, `order`.`order_date` = '" + date +"', `order`.`order_time` = '" + time + "', `order`.`order_total` = (SELECT SUM(orderdetail.orderdetail_qty*orderdetail.orderdetail_price) FROM orderdetail WHERE orderdetail.order_id = (SELECT `order`.order_id FROM `order` WHERE `order`.`user_id` = (SELECT user.user_id FROM user WHERE user.user_username =  '" + username +"') AND `order`.order_status = 0)) WHERE `order`.`order_id` = (SELECT `order`.order_id FROM `order` WHERE `order`.`user_id` = (SELECT user.user_id FROM user WHERE user.user_username =  '" + username +"') AND `order`.order_status = 0)"
    dbConn.query(confirm, function(error, results, fileds){
        if (results){
            return res.send(results)
        }else{
            throw error;
        }
    })
})

app.get('/receiptdetail/:order_id', function(req,res){
    var order_id = req.params.order_id
    if (!order_id){
        return res.status(400).send({ error:true, message: 'No Order ID Founded'});
    }
    dbConn.query("SELECT order_date, order_total FROM `order` WHERE order_id = ?", [order_id] , function(error, results, fields){
        if (results[0]){
            return res.send(results[0])
        }else{
            return res.status(400).send({ error: true, message: 'Order Not Found !' })
        }
    })
})

app.get('/receiptproduct/:order_id', function(req,res){
    var order_id = req.params.order_id
    if (!order_id){
        return res.status(400).send({ error:true, message: 'No Order ID Founded'});
    }
    dbConn.query("SELECT orderdetail.orderdetail_qty,product.product_name, orderdetail.orderdetail_qty*orderdetail.orderdetail_price AS total FROM orderdetail,product WHERE orderdetail.order_id = ? AND product.product_id = orderdetail.product_id", [order_id], function(error, results, field){
        if (results){
            return res.send(results)
        }else{
            return res.status(400).send({ error: true, message: 'Order Not Found !' })
        }
    })
})

app.put('/addreceipt/:order_id', function(req, res) {
    var img_name = req.body.img_name
    var order_id = req.params.order_id
    var tranfer_time = req.body.tranfer_time
    if (!img_name){
        return res.status(400).send({ error:true, message: 'No Image Founded'});
    }
    dbConn.query("UPDATE `order` SET order_status = 2, receipt_img = ?, receipt_time = ? WHERE order_id = " + order_id, [img_name, tranfer_time], function(error, results, fields){
        if (results){
            dbConn.query("SELECT orderdetail.product_id FROM orderdetail,product WHERE orderdetail.order_id = ? AND product.product_id = orderdetail.product_id", [order_id], function(err, ress, field){
                if (ress){
                    for (let i = 0 ; i < ress.length; i++){
                        var product = ress[i].product_id
                        dbConn.query("UPDATE product SET product.product_qty = product.product_qty - (SELECT orderdetail.orderdetail_qty FROM orderdetail WHERE orderdetail.product_id = ? AND orderdetail.order_id = ?) WHERE product.product_id = ?", [product, order_id, product], function(er, re, fi){
                            if (re){
                                console.log(product)
                            }else{
                                console.log(product)
                            }
                        })
                    }
                }
            })
            return res.send(results)
        }else{
            return res.status(400).send({ error: true, message: 'Upload Failed !!!' })
        }
    })
})

app.post('/addproduct/', function(req, res) {
    let data = req.body

    let productName = data.product_name
    let productPrice = data.product_price
    let productDetail = data.product_detail
    let productImg = data.product_img
    let productQty = data.product_qty
    let categoryId = data.category_id

    dbConn.query('INSERT INTO `product`(`product_name`, `product_price`, `product_detail`, `product_img`, `product_qty`, `category_id`) VALUES (?, ?, ?, ?, ?, ?)', [productName, productPrice, productDetail, productImg, productQty, categoryId], function(error, results, fields) {
        if(error) throw error;
        return res.send(results);
    });
});

app.post('/editproduct/', function(req, res) {
    let data = req.body
    let productName = data.product_name
    let productPrice = data.product_price
    let productDetail = data.product_detail
    let productImg = data.product_img
    let productQty = data.product_qty
    let categoryId = data.category_id

    dbConn.query('UPDATE product SET `product_name` = ?, `product_price` = ?, `product_detail` = ?, `product_img` = ?, `product_qty` = ?, `category_id` = ? WHERE product_name = ?', [productName, productPrice, productDetail, productImg, productQty, categoryId, productName], function(error, results, fields) {
        if(error) throw error;
        return res.send(results);
    });
});

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
    dbConn.query('SELECT `user`.`user_id`, `user`.`user_name`, `user`.`user_address`, `order`.`order_id`, `order`.`order_date`, `order`.`order_total`, `order`.`order_status`, `order`.`order_tracking` FROM `user`, `order` WHERE `user`.`user_id` = `order`.`user_id` AND `order`.`order_status` = 3 AND `order`.`order_tracking` != 0', function(error, results, fields) {
        if(error) throw error;
        return res.send(results);
    });
});

app.patch('/updatestatusdelivery/:order_id', function(req, res) {
    let orderId = req.params.order_id
    let statusDelivery = req.body.delivery_status
    let receivedTime = req.body.order_received_time

    if(!orderId){
        res.status(400).send({ error: true, message: "Please provide order id" })
    } else {
        if(statusDelivery == 2){
            dbConn.query('UPDATE `order` SET `order_received_time` = ?, `order_status` = 3, `order_tracking` = ? WHERE `order`.`order_id` = ?', [receivedTime, statusDelivery, orderId], function(error, results, fields) {
                if(error) throw error;
                return res.send(results);
            })
        } else if(statusDelivery == 3) {
            dbConn.query('UPDATE `order` SET `order_received_time` = ?, `order_status` = 4, `order_tracking` = ? WHERE `order`.`order_id` = ?', [receivedTime, statusDelivery, orderId], function(error, results, fields) {
                if(error) throw error;
                return res.send(results);
            })
        } else {
            res.status(400).send({ error: true, message: "statusDelivery wrong." })
        }
    }
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
            res.status(400).send({ error: true, message: "User already exist." })
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

app.put('/edituserprofile/:username', function(req, res){
    var username = req.params.username
    var name = req.body.name
    var tel = req.body.tel
    var email = req.body.email
    var address = req.body.address
    var password = req.body.password
    var confirm = req.body.confirm
    dbConn.query("SELECT * FROM user WHERE user_username = ?", [username], function(err, result, field){
        if (result[0]){
            var userPassword = result[0].user_password

            if (password == confirm && password == userPassword){
                dbConn.query("UPDATE user SET user_name = ? , user_tel = ? , user_address = ? , user_email = ? WHERE user_username = ?", [name, tel, address, email, username], function(err, results, fields) {
                    if (results){
                        return res.send(results)
                    }else{
                        return res.status(400).send({error: true, message: "Update Failed !!!"})
                    }
                })
            }else{
                return res.status(400).send({error: true, message: "Wrong Password !!!"})
            }
        }
    })

})

app.patch('/editprofileadmin/', function(req, res) {
    let data = req.body;

    let userID = data.user_id;
    let nameUser = data.user_name;
    let userEmail = data.user_email;
    let userTel = data.user_tel;
    let userAddress = data.user_address;
    let userUsername = data.user_username;
    let userPassword = data.user_password;
    let userCfPassword = data.user_cfpassword;
    let cfPassword = false

    if(userPassword == userCfPassword){
        cfPassword = true ;
    } else {
        cfPassword = false ;
    }

    dbConn.query('SELECT * FROM `user` WHERE `user_id` = ?', userID, function(error, results, fields) {
        if(error) throw error;

        if(results && results.length){
            if(cfPassword == true){
                dbConn.query('UPDATE `user` SET `user_username` = ?, `user_password` = ?, `user_tel` = ?, `user_address` = ?, `user_email` = ?, `user_name` = ? WHERE `user_id` = ?', [userUsername, userPassword, userTel, userAddress, userEmail, nameUser, userID], function(error, results, fields) {
                    if(error) throw error;

                    res.send(results);
                })
            } else {
                res.status(400).send({ error: true, message: "Password does not match." })
            }
        } else {
            res.status(400).send({ error: true, message: "User not exist." })
        }
    });
});

app.post('/addstaff/', function(req, res) {
    let data = req.body;

    let nameUser = data.user_name;
    let userEmail = data.user_email;
    let userTel = data.user_tel;
    let userAddress = data.user_address;
    let userUsername = data.user_username;
    let userType = data.user_type;
    let userPassword = data.user_password;
    let userCfPassword = data.user_cfpassword;

    if(userPassword == userCfPassword){
        dbConn.query('SELECT * FROM `user` WHERE `user_username` = ?', userUsername, function(error, results, fields) {
            if(error) throw error;

            if(results && results.length){
                res.status(400).send({ error: true, message: "User already exist." })
            }else{
                dbConn.query('INSERT INTO `user`(`user_username`, `user_password`, `user_tel`, `user_address`, `user_email`, `user_name`, `user_type`) VALUES (?,md5(?),?,?,?,?,?)', [userUsername, userPassword, userTel, userAddress, userEmail, nameUser, userType], function(error, results, fields) {
                    if(error) throw error;
                    res.send(results);
                });
            }
        });
    } else {
        res.status(400).send({ error: true, message: "Password and Confirm Password does not match." })
    }
});

app.get('/getalluserorder/:username', function(req,res){
    var username = req.params.username
    dbConn.query("SELECT * FROM `order` WHERE `order`.`user_id` = (SELECT `user`.`user_id` FROM `user` WHERE `user`.`user_username` = ?) and `order`.`order_status` NOT IN (0,1,2) ORDER BY `order`.`order_id` DESC", [username], function(error, results, fields){
        if (results){
            return res.send(results)
        }else{
            return res.status(400).send({error: true, message: "No order found"})
        }
    })
})

app.listen(3000, function() {
    console.log('Node app is running on port 3000');
});

module.exports = app;