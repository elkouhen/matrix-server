extern crate iron;
extern crate router;
extern crate bodyparser;
extern crate persistent;
extern crate rustc_serialize;

//use persistent::Read;
//use rustc_serialize::serialize::Decodable;
use iron::status;
use iron::prelude::*;
use router::{Router};
use rustc_serialize::json;

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

fn main() {
    let mut router = Router::new();
    router.post("/matrix/power", handler);
    
    Iron::new(router).http("localhost:3000").unwrap();

    fn handler(req: &mut Request) -> IronResult<Response> {
        
        let body = req.get::<bodyparser::Struct<PowerOperation>>();

	let vector = vec![1, 0, 0, 0, 1, 0, 0, 0, 1]; 

	let matrix = Matrix { nx : 3, data : vector}; 
	
	let power_operation = PowerOperation { right : 3, left : matrix}; 
 
	let power_operation_encoded = json::encode(&power_operation).unwrap(); 

	Ok(Response::with((status::Ok, power_operation_encoded)))
    }
}
