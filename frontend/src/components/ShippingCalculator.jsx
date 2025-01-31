import React, { useState } from "react";

const ShippingCalculator = () => {
  const [destination, setDestination] = useState("");
  const [result, setResult] = useState(null);

  const fetchShippingOptions = async () => {
    const response = await fetch(
      `http://localhost:8080/api/shipping?destination=${destination}`
    );
    const data = await response.json();
    console.log(data);
    setResult(data);
  };

  return (
    <div style={styles.container}>
      <h2>Shipping Calculator</h2>
      <div style={styles.inputContainer}>
        <select
          value={destination}
          onChange={(e) => setDestination(e.target.value)}
          style={styles.select}
        >
          <option value="">Select Destination</option>
          <option value="Delhi">Delhi</option>
          <option value="Mumbai">Mumbai</option>
          <option value="Bangalore">Bangalore</option>
          <option value="Kolkata">Kolkata</option>
          <option value="Chennai">Chennai</option>
        </select>
        <button onClick={fetchShippingOptions} style={styles.button}>
          Calculate
        </button>
      </div>

      {result && (
        <div style={styles.resultContainer}>
          <h3>Shipping Options:</h3>
          <table style={styles.table}>
            <thead>
              <tr>
                <th style={styles.th}>Option</th>
                <th style={styles.th}> </th>
               
              </tr>
            </thead>
            <tbody>
              <tr>
                <td style={styles.td}>Best Possible Option</td>
                <td style={styles.td}>{result["Best Overall Option"]}</td>
              
              </tr>
              <tr>
                <td style={styles.td}>Cheapest Option</td>
                <td style={styles.td}>{result["Best Cost Option"]}</td>
              
              </tr>
              <tr>
                <td style={styles.td}>Quickest Option</td>
                <td style={styles.td}>{result["Best Time Option"]}</td>
              
              </tr>
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

const styles = {
  container: {
    fontFamily: "'Arial', sans-serif",
    padding: "20px",
    maxWidth: "800px",
    margin: "auto",
    backgroundColor: "#f9f9f9",
    borderRadius: "8px",
    boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
  },
  inputContainer: {
    marginBottom: "20px",
  },
  select: {
    padding: "10px",
    marginRight: "10px",
    width: "50%",
    borderRadius: "5px",
    border: "1px solid #ccc",
    fontSize: "16px",
  },
  button: {
    padding: "10px 20px",
    backgroundColor: "#077b32",
    color: "white",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer",
    fontSize: "16px",
  },
  resultContainer: {
    marginTop: "30px",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
    border: "1px solid #ddd",
  },
  th: {
    padding: "12px 15px",
    textAlign: "left",
    backgroundColor: "#077b32",
    color: "white",
    border: "1px solid #ddd",
  },
  td: {
    padding: "12px 15px",
    textAlign: "left",
    borderBottom: "1px solid #ddd",
    borderRight: "1px solid #ddd",
  },
};

export default ShippingCalculator;
