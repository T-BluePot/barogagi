/** @type {import('tailwindcss').Config} */
export const content = ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"];
export const theme = {
  extend: {
    colors: {
      primary: "#1c7ed6",
      secondary: "#f06595",
      neutral: "#495057",
    },
    fontFamily: {
      sans: ["'Noto Sans KR'", "sans-serif"],
    },
  },
};
export const plugins = [];
