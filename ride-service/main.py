from fastapi import FastAPI, HTTPException
from models import RideRequest, RideResponse, Ride
from storage import rides, user_ride_history
from services import book_ride_logic

app = FastAPI()

@app.post("/book_ride/", response_model=RideResponse)
def book_ride(request: RideRequest):
    return book_ride_logic(request)

@app.get("/ride_status/{ride_id}", response_model=RideResponse)
def ride_status(ride_id: str):
    ride = rides.get(ride_id)
    if not ride:
        raise HTTPException(status_code=404, detail="Ride not found")
    return RideResponse(
        ride_id=ride.ride_id,
        status=ride.status,
        driver_id=ride.driver_id
    )

@app.post("/complete_ride/{ride_id}")
def complete_ride(ride_id: str):
    ride = rides.get(ride_id)
    if not ride:
        raise HTTPException(status_code=404, detail="Ride not found")
    ride.status = "completed"
    return {"message": f"Ride {ride_id} marked as completed"}

@app.get("/ride_history/{user_id}")
def ride_history(user_id: str):
    history = [rides[rid] for rid in user_ride_history.get(user_id, [])]
    return {"user_id": user_id, "rides": history}
