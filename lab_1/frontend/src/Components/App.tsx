import { useState } from 'react'
import './App.css'
import Table from './Table.tsx'

function App() {
  const [state, setState] = useState({
    entity: "LabWork"
  })

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
          <div className={`menu-button-container ${state.entity === "LabWork" ? "menu-button-chosen" : ""}`}>
            <button className='menu-button' onClick={() => {setState({...state, entity: "LabWork"})}}>
              LabWork
            </button>
          </div>
           <div className={`menu-button-container ${state.entity === "Person" ? "menu-button-chosen" : ""}`}>
            <button className='menu-button' onClick={() => {setState({...state, entity: "Person"})}}>
              Person
            </button>
          </div>
         <div className={`menu-button-container ${state.entity === "Location" ? "menu-button-chosen" : ""}`}>
            <button className='menu-button' onClick={() => {setState({...state, entity: "Location"})}}>
              Location
            </button>
          </div>
          <div className={`menu-button-container ${state.entity === "Discipline" ? "menu-button-chosen" : ""}`}>
            <button className='menu-button' onClick={() => {setState({...state, entity: "Discipline"})}}>
              Discipline
            </button>
          </div>
          <div className={`menu-button-container ${state.entity === "Coordinate" ? "menu-button-chosen" : ""}`}>
            <button className='menu-button' onClick={() => {setState({...state, entity: "Coordinate"})}}>
              Coordinate
            </button>
          </div>
        </div>
        <div className='menu-extras'>
          <div className={`menu-button-container ${state.entity === "Additional" ? "menu-button-chosen" : ""}`}>
            <button className='menu-button' onClick={() => {setState({...state, entity: "Additional"})}}>
              Доп. функции
            </button>
          </div>
        </div>
      </div>
      <div className='table-space'>
        <Table object={state.entity}>

        </Table>
      </div>
    </>
  )
}

export default App
