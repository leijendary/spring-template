import { check } from "k6";
import exec from "k6/execution";
import http from "k6/http";

const url = "http://localhost:30080";

// Ramp up to 500 VUs within 30 seconds.
// Then continue to run for 5 minutes with the same number of VUs.
export const options = {
  stages: [
    {
      duration: "30s",
      target: 500,
    },
    {
      duration: "5m",
      target: 500,
    },
  ],
};

export default function () {
  const id = create();
  get(id);
  update(id);
  remove(id);
}

function create() {
  const iteration = exec.scenario.iterationInInstance + 1;
  const payload = JSON.stringify({
    name: `Test ${iteration}`,
    description: `I am testing iteration #${iteration}`,
    amount: iteration * 1.99,
    translations: [
      {
        name: `Test english ${iteration}`,
        language: "en",
        ordinal: 1,
      },
      {
        name: `Test japanese ${iteration}`,
        description: `Testing the japanese description ${iteration}`,
        language: "jp",
        ordinal: 2,
      },
    ],
  });
  const params = {
    headers: {
      "Content-Type": "application/json",
    },
  };
  const res = http.post(`${url}/api/v1/samples/admin`, payload, params);

  check(res, {
    "create status is 201": (r) => r.status === 201,
  });

  const json = res.json();

  return json.id;
}

function get(id) {
  const params = {
    headers: {
      "Content-Type": "application/json",
    },
  };
  const res = http.get(`${url}/api/v1/samples/admin/${id}`, params);

  check(res, {
    "get status is 200": (r) => r.status === 200,
  });
}

function update(id) {
  const payload = JSON.stringify({
    name: `Test updated ${id}`,
    description: `I am testing updated record ${id}`,
    amount: 9999999.99,
    translations: [
      {
        name: `Test dutch updated ${id}`,
        description: `Testing the dutch description ${id}`,
        language: "nl",
        ordinal: 1,
      },
      {
        name: `Test english updated ${id}`,
        language: "en",
        ordinal: 2,
      },
      {
        name: `Test japanese updated ${id}`,
        description: `Testing the japanese description updated ${id}`,
        language: "jp",
        ordinal: 3,
      },
    ],
  });
  const params = {
    headers: {
      "Content-Type": "application/json",
    },
  };
  const res = http.put(`${url}/api/v1/samples/admin/${id}?version=0`, payload, params);

  check(res, {
    "update status is 200": (r) => r.status === 200,
  });
}

function remove(id) {
  const res = http.del(`${url}/api/v1/samples/admin/${id}?version=1`);

  check(res, {
    "update status is 204": (r) => r.status === 204,
  });
}
