from uuid import uuid4
from models import Ride, RideRequest, RideResponse
from storage import rides, user_ride_history, drivers

def match_driver(pickup_location: str) -> str:
    # Dummy match: return first driver available
    return next(iter(drivers), None)

def book_ride_logic(request: RideRequest) -> RideResponse:
    ride_id = str(uuid4())
    driver_id = match_driver(request.pickup_location)
    status = "accepted" if driver_id else "pending"

    ride = Ride(
        ride_id=ride_id,
        user_id=request.user_id,
        pickup_location=request.pickup_location,
        dropoff_location=request.dropoff_location,
        driver_id=driver_id,
        status=status
    )

    rides[ride_id] = ride
    user_ride_history.setdefault(request.user_id, []).append(ride_id)

    return RideResponse(ride_id=ride_id, status=status, driver_id=driver_id)
