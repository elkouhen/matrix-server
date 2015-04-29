extern crate bodyparser;
extern crate hyper;
extern crate iron;
extern crate persistent;
extern crate router;
extern crate rustc_serialize;

use hyper::Client;
use hyper::header::*;
use iron::status;
use iron::prelude::*;
use router::{Router};
use rustc_serialize::json;
use std::io::Read;

#[derive(Debug, Clone, RustcDecodable, RustcEncodable)]
struct Matrix {
    nx : i32, 
    data : Vec<i32>,
}

#[derive(Debug, Clone, RustcDecodable, RustcEncodable)]
struct PowerOperation {
    right: i32,
    left: Matrix,
}

fn matrix_mult (m : &Matrix, n : &Matrix) -> Matrix {

    let nx = (*m).nx; 

    let mut vec : Vec<i32> = Vec::with_capacity(nx as usize);

    for i in 0..nx {
        for j in 0..nx {

            let mut sum = 0;
            for k in 0..nx {
                sum += (*m).data[(i * nx + j) as usize] * (*n).data[(k * nx + j) as usize];
            }

            vec[(i * nx + j) as usize] = sum;
        }
    }
    
    let result_matrix = Matrix { nx : nx, data : vec};

    result_matrix
}

fn main() {
    let mut router = Router::new();
    router.post("/matrix/power", handler);
    
    Iron::new(router).http("localhost:3000").unwrap();

    fn handler(req: &mut Request) -> IronResult<Response> {

        println!("request 0.1");

        let operation_param = req.get::<bodyparser::Struct<PowerOperation>>();
      
        println!("request 0.1 {:?}" , operation_param);

        let operation : PowerOperation = operation_param.unwrap().unwrap(); 

        println!("request 0.3 {:?}" , operation);

        if operation.right == 1 {

            println!("1.1");
            
            let response = json::encode(&operation.left).unwrap(); 

            Ok(Response::with((status::Ok, response)))
        }
        else {

            
            let operation_n1 = PowerOperation { right : operation.right -1, left : operation.left};

            let operation_n1_serialize = json::encode(&operation_n1).unwrap(); 

            println!("2.1 {:?}", operation_n1_serialize); 
            
            let mut client = Client::new();
            
            let mut res = client.post("http://localhost:3000/matrix/power")
                .body(&operation_n1_serialize[..])
                .send().unwrap();

            println!("2.2");

            let mut reponse_post = String::new();

            res.read_to_string(&mut reponse_post); 

            println!("2.3 {}", reponse_post); 

	    let matrix_n1 : Matrix = json::decode(&reponse_post).unwrap(); 

            let result_multi = matrix_n1; // matrix_mult(&operation.left, &matrix_n1); 

            let response = json::encode(&result_multi).unwrap(); 

            Ok(Response::with((status::Ok, response)))
        }
    }
}
