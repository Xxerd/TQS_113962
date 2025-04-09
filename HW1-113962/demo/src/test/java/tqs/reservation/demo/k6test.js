import { check } from "k6";
import http from "k6/http";

const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";

export const options = {
    stages: [
        { duration: "30s", target: 50 }, 
        { duration: "1m", target: 50 },
        { duration: "30s", target: 0 },  
    ],
    thresholds: {
        http_req_failed: ["rate<0.05"], // HTTP errors should be less than 2%
        http_req_duration: ["p(95)<1400"], // 95% of requests should be below 1.1s
    },
};

export default function () {

    testGetAllRestaurants();
    testCreateRestaurant();

    testGetAllMeals();
    testCreateMeal();

    testCreateReservation();

    testGetWeatherForecast();
}

function testGetAllRestaurants() {
    let res = http.get(`${BASE_URL}/apiV1/restaurants`);
    check(res, {
        "Get all restaurants - status is 200": (r) => r.status === 200,
        "Get all restaurants - response time < 1000ms": (r) => r.timings.duration < 1000,
    });
}

function testCreateRestaurant() {
    let payload = JSON.stringify({
        name: "Test Restaurant",
        latitude: 40.123456,
        longitude: -8.654321,
        maxCapacity: 50,
    });

    let res = http.post(`${BASE_URL}/apiV1/restaurants/create`, payload, {
        headers: { "Content-Type": "application/json" },
    });

    check(res, {
        "Create restaurant - status is 201": (r) => r.status === 201,
        "Create restaurant - response time < 1000ms": (r) => r.timings.duration < 1000,
    });
}

function testGetAllMeals() {
    let res = http.get(`${BASE_URL}/apiV1/meals`);
    check(res, {
        "Get all meals - status is 200": (r) => r.status === 200,
        "Get all meals - response time < 1000ms": (r) => r.timings.duration < 1000,
    });
}

function testCreateMeal() {
    let payload = JSON.stringify({
        name: "Test Meal",
        description: "Delicious test meal",
        price: 15.0,
        restaurant: { id: 1 }, // Replace with a valid restaurant ID
        date: "2025-04-12",
        numberOfMeals: 10,
        reservationTime: "LUNCH",
    });

    let res = http.post(`${BASE_URL}/apiV1/meals/create`, payload, {
        headers: { "Content-Type": "application/json" },
    });

    check(res, {
        "Create meal - status is 201": (r) => r.status === 201,
        "Create meal - response time < 1000ms": (r) => r.timings.duration < 1000,
    });
}

function testCreateReservation() {
    let payload = JSON.stringify({
        customerName: "John Doe",
        customerEmail: "john.doe@example.com",
        reservationTime: "LUNCH",
        meal: { id: 8555},
    });

    let res = http.post(`${BASE_URL}/apiV1/reservations/create`, payload, {
        headers: { "Content-Type": "application/json" },
    });

    check(res, {
        "Create reservation - status is 201": (r) => r.status === 201,
        "Create reservation - response time < 1000ms": (r) => r.timings.duration < 1000,
    });
}

function testGetWeatherForecast() {
    let res = http.get(`${BASE_URL}/apiV1/weather/forecast?latitude=40.0&longitude=-8.0&day=2025-04-10`);
    check(res, {
        "Get weather forecast - status is 200": (r) => r.status === 200,
        "Get weather forecast - response time < 1000ms": (r) => r.timings.duration < 1000,
    });
}