extern crate iron;
extern crate router;
extern crate bodyparser;
extern crate persistent;
extern crate serialize;
extern crate rustc_serialize;

use persistent::Read;
//use rustc_serialize::serialize::Decodable;
use iron::status;
use iron::prelude::*;
use router::{Router};

#[derive(Debug, Clone, RustcDecodable)]
struct Matrix {
       nx : i32, 
       //data : [i32; 9],
}

#[derive(Debug, Clone, RustcDecodable)]
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

	Ok(Response::with((status::Ok, "Hello world")))
    }
}
