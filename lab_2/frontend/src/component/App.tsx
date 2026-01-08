import { useState } from 'react'
import '../style/App.css'
import Table from './Table.tsx'
import Extras from './Extras.tsx'
import BaseFrame from './frame/BaseFrame'
import ImportModal from './frame/ImportModal'

function App() {
  const [entity, setEntity] = useState("LabWork")
  const [importModalOpen, setImportModalOpen] = useState(false)
  

  return (
    <>
      <header>
        <div className='header-container'>
          <div className="main-title"><h2>Лабораторная работа №2</h2></div>
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
          <div className='menu-button-container'>
            <button 
              className='menu-button' 
              onClick={() => setImportModalOpen(true)}
            >
              Импорт
            </button>
          </div>
        </div>
      </div>
      
      {/* Модальное окно импорта */}
      <BaseFrame 
        isOpen={importModalOpen} 
        onClose={() => setImportModalOpen(false)} 
        zindex={1000} 
        width="65%" 
        height="80%"
      >
        <ImportModal onClose={() => setImportModalOpen(false)} />
      </BaseFrame>
      
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