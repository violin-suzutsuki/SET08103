import React from "react";
import { useState } from "react";

function InputsBar({ setTableData, setTableTpl, setLoading }) {
  const [inputs, setInputs] = useState({id: "1", userInput: ""}) 
  const customizableReports = ["3", "5", "6", "12", "13", "14", "15", "16", "21", "22", "23"]
  
  // setTableData is from the App
  const getData = (id, userInput) => {
    setTableTpl(id)
    setLoading(true)
    const responseFromAPI = fetch(`/api/report?reportId=${id}&userInput=${userInput}`)
      .then((response) => response.json())
      .then((responseJSON) => {
        // do stuff with responseJSON here...
        console.log(responseJSON);
        setTableData(responseJSON);
      });
    setLoading(false);
  };

  const hideShowInput = (id) => {
    var input = document.getElementById("input");
    input.disabled = !customizableReports.includes(id);
    (customizableReports.includes(id)) ? input.parentElement.style.display = "block" : input.parentElement.style.display = "none" 
  };

  const handleSubmit = Event => {
    Event.preventDefault();
    getData(inputs.id, inputs.userInput)
    document.getElementById("input").value="";
  }

  return (
    <div className="container mx-auto py-12">
      <form className="flex items-center justify-center flex-col md:space-x-4 md:flex-row" onSubmit={handleSubmit}>
        <div className="mb-4 w-full md:w-auto">
          <label>Report Type</label>
          <select className="mt-1 select w-full max-w-full md:max-w-xs font-medium border-2 border-indigo-500/100" required onChange={e => {setInputs(values => ({...values, id: e.target.value})); hideShowInput(e.target.value)}}>
            <option selected hidden disabled>Please select a report</option>
            <optgroup label="Country Report">
              <option value="22">22</option>
            </optgroup>
            <optgroup label="City Report">
              <option value="23">23</option>
            </optgroup>
            <optgroup label="Capital Report">
              <option value="24">24</option>
            </optgroup>
            <optgroup label="Population Report">
              <option value="25">25</option>
            </optgroup>
          </select>
        </div>
        
        <div style={{display: 'none'}} className="mb-4 md:w-auto w-full">
          <label>User input</label>
          <input className="mt-1 input w-full max-w-full md:max-w-xs border-2 border-indigo-500/100 re" onBlur={(e) => setInputs(values => ({...values, userInput: e.target.value}))} type="text" id="input" disabled required/>
        </div>

        <div className="mt-2.5 w-full md:w-auto">
          <button className="btn btn-success w-full md:w-auto" type="submit">Generate</button>
        </div>

      </form>
    </div>
  );
}

export default InputsBar;
