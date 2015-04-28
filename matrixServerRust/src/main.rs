extern crate iron;
extern crate router;
extern crate bodyparser;
extern crate persistent;
extern crate rustc_serialize;

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

fn matrix_mult (m : Matrix, n : Matrix) -> Matrix {

    let nx = m.nx; 

    let mut vec : Vec<i32> = Vec::with_capacity(nx as usize);

    for i in 0..nx {
        for j in 0..nx {

            let mut sum = 0;
            for k in 0..nx {
                sum += m.data[(i * nx + j) as usize] * n.data[(k * nx + j) as usize];
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
        
        let operation_param = req.get::<bodyparser::Struct<PowerOperation>>();
      
        let operation : PowerOperation = operation_param.ok().unwrap().unwrap(); 

        //let mut response; 

        if operation.right == 1 {
            
            let response = json::encode(&operation.left).unwrap(); 

            Ok(Response::with((status::Ok, response)))
        }
        else {
            
            let vector = vec![1, 0, 0, 0, 1, 0, 0, 0, 1]; 

	    let matrix = Matrix { nx : 3, data : vector}; 
	    
	    let power_operation = PowerOperation { right : 3, left : matrix}; 
            
	    let response = json::encode(&power_operation).unwrap(); 

            Ok(Response::with((status::Ok, response)))
        }
    }
}
