import { ThemeProvider } from "styled-components";
import Background from "./components/Background";
import LoginForm from "./components/LoginForm";
import GlobalStyles from "./components/styles/GlobalStyles";
import RegistrationForm from "./components/RegistrationForm";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { useState, useEffect } from "react";
import HomePage from "./components/HomePage";

const theme = {
  colors: {
    header: '#ebfbff',
    body: '#080710',
    footer: '#003333'
  },

  mobile: '768px',
}
function App() {
  const [user, setUser] = useState()
  const [showLogin, setShowLogin] = useState(true)
  const performLogin = async ({ username, password }) => {

    console.log(username, password)
    const res = await fetch(`http://localhost:8080/api/auth/signin`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Access-Control-Allow-Origin' : 'http://localhost:3000'
      },
      body: JSON.stringify({
        username,
        password 
      }),
    })

    const data = await res.json()
    console.log(data)
  }
  return (
    <Router>
    <ThemeProvider theme = {theme}>
        <GlobalStyles />
          
    <Routes>
          <Route
            path="/"
            element={
              <>
                <HomePage />
              </>
            }
            />
          <Route
          path="/signin"
          element={<>
            <Background />
            {showLogin && <LoginForm onSubmitForm = {performLogin} />}
            </>}
          />

          <Route
          path="/register"
          element={<>
            <Background />
            <RegistrationForm onSubmitForm = {performLogin} />
            </>}
          />
    </Routes>
    </ThemeProvider>
    </Router>
  );
}

export default App;
