from pydantic import BaseModel
from typing import Optional

class RideRequest(BaseModel):
    user_id: str
    pickup_location: str
    dropoff_location: str

class Ride(BaseModel):
    ride_id: str
    user_id: str
    pickup_location: str
    dropoff_location: str
    driver_id: Optional[str]
    status: str  # pending, accepted, completed

class RideResponse(BaseModel):
    ride_id: str
    status: str
    driver_id: Optional[str]
