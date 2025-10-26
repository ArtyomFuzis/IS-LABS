import { useState } from 'react'
import '../Styles/App.css'
import Table from './Table.tsx'
import Extras from './Extras.tsx'

function App() {
  const [entity, setEntity] = useState("LabWork")
  

  return (
    <>
      <header>
        <div className='header-container'>
          <div className="main-title"><h2>Лабораторная работа №1</h2></div>
          <div className="sub-title">Выполнил: Назин Артем Аркадьевич, P3307</div>
        </div>
      </header>
      <div className='menu-level'>
        <div className='menu-container'>
          <div className={`menu-button-container ${entity === "LabWork" ? "menu-button-chosen" : ""}`}>
            <button className='menu-button' onClick={() => {setEntity("LabWork")}}>
              LabWork
            </button>
          </div>
           <div className={`menu-button-container ${entity === "Person" ? "menu-button-chosen" : ""}`}>
            <button className='menu-button' onClick={() => {setEntity("Person")}}>
              Person
            </button>
          </div>
         <div className={`menu-button-container ${entity === "Location" ? "menu-button-chosen" : ""}`}>
            <button className='menu-button' onClick={() => {setEntity("Location")}}>
              Location
            </button>
          </div>
          <div className={`menu-button-container ${entity === "Discipline" ? "menu-button-chosen" : ""}`}>
            <button className='menu-button' onClick={() => {setEntity("Discipline")}}>
              Discipline
            </button>
          </div>
          <div className={`menu-button-container ${entity === "Coordinate" ? "menu-button-chosen" : ""}`}>
            <button className='menu-button' onClick={() => {setEntity("Coordinate")}}>
              Coordinate
            </button>
          </div>
        </div>
        <div className='menu-extras'>
          <div className={`menu-button-container ${entity === "Additional" ? "menu-button-chosen" : ""}`}>
            <button className='menu-button' onClick={() => {setEntity("Additional")}}>
              Доп. функции
            </button>
          </div>
        </div>
      </div>
      {entity=="Additional" ? (
        <Extras/>
      ) : (
        <div className='table-space'>
          <Table key={entity} objectName={entity}/>
        </div>
      )}
      
    </>
  )
}

export default App
