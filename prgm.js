const fs = require("fs");

// Read the JSON file
const data = JSON.parse(fs.readFileSync("output.json", "utf8"));

// Extract n and k
const n = data.keys.n;
const k = data.keys.k;

console.log(`n = ${n}, k = ${k}`);

// Extract points from "roots" array
let points = data.roots.map(root => [root.x, root.y]);

console.log("Decoded points:", points);

// Solve for a, b, c using the first 3 points
function solveABC(points) {
  if (points.length < 3) {
    throw new Error("Need at least 3 points to solve for a, b, c");
  }

  const [p1, p2, p3] = points;

  function equation([x, y]) {
    return [Math.pow(x, n), Math.pow(x, n - 1), 1, y];
  }

  const eq1 = equation(p1);
  const eq2 = equation(p2);
  const eq3 = equation(p3);

  const A = [
    [eq1[0], eq1[1], 1],
    [eq2[0], eq2[1], 1],
    [eq3[0], eq3[1], 1],
  ];
  const B = [eq1[3], eq2[3], eq3[3]];

  function det3(m) {
    return (
      m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1]) -
      m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0]) +
      m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0])
    );
  }

  const D = det3(A);
  const Da = det3([
    [B[0], A[0][1], A[0][2]],
    [B[1], A[1][1], A[1][2]],
    [B[2], A[2][1], A[2][2]],
  ]);

  const Db = det3([
    [A[0][0], B[0], A[0][2]],
    [A[1][0], B[1], A[1][2]],
    [A[2][0], B[2], A[2][2]],
  ]);

  const Dc = det3([
    [A[0][0], A[0][1], B[0]],
    [A[1][0], A[1][1], B[1]],
    [A[2][0], A[2][1], B[2]],
  ]);

  const a = Da / D;
  const b = Db / D;
  const c = Dc / D;

  return { a, b, c };
}

const { a, b, c } = solveABC(points);

console.log("\nSolved coefficients:");
console.log("a =", a);
console.log("b =", b);
console.log("c =", c);

console.log("\n Final c value:", c);
