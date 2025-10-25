import React, { useEffect, useState } from "react"
import {makeQuerySelect } from "../../../Utils/baseMaps"
import {fetchData } from "../../../Utils/apiQueries"
import type { SelectDTO } from "../../../interfaces/DTO/SelectDTO"
import type { TableData } from "../../../interfaces/TableData"
import CoordinateTable from "../../Tables/CoordinateTable"
import DisciplineTable from "../../Tables/DisciplineTable"
import LocationTable from "../../Tables/LocationTable"
import PersonTable from "../../Tables/PersonTable"
import LabWorkTable from "../../Tables/LabWorkTable"
import type { LabWork } from "../../../interfaces/Entities/LabWork"
import type { Person } from "../../../interfaces/Entities/Person"
import type { Coordinate } from "../../../interfaces/Entities/Coordinate"
import type { Discipline } from "../../../interfaces/Entities/Discipline"
import type { Location } from "../../../interfaces/Entities/Location"
import BaseFrame from "../BaseFrame"
import CreateLocationSF from "./CreateLocationSF"
import CreateCoordinateSF from "./CreateCoordinateSF"
import CreateDisciplineSF from "./CreateDisciplineSF"
import CreateLabWorkSF from "./CreateLabWorkSF"
import CreatePersonSF from "./CreatePersonSF"

function ChooseSF({ onClose, objectName, chosenRow, setChosenRow, required }: { onClose: () => void, objectName: string, chosenRow: number,  setChosenRow: React.Dispatch<number>, required: boolean}) {
    const params = {objectName}
    const [data, setData] = useState({success: false});
    const [filterColumn, setFilterColumn] = useState("");
    const [filterData,   setFilterData]   = useState("");
    const [sortColumn, setSortColumn] = useState("");
    const [page, setPage] = useState(1);
    const [reversedSorting, setReversedSorting] = useState(false);
    const [nextPageExists,  setNextPageExists] = useState(false);
    const [modalOpen,  setModalOpen] = useState("");
    const loadData = async () => {
          const result = await fetchData(makeQuerySelect(params.objectName,filterColumn,filterData,sortColumn,page,reversedSorting) )
          setData(result)
          const result_next : SelectDTO<any> = await fetchData(makeQuerySelect(params.objectName,filterColumn,filterData,sortColumn,page+1,reversedSorting))
          setNextPageExists(result_next.result.length !== 0)
          transfered_data = {data: (data as SelectDTO<any>), filterColumn, filterData, sortColumn, reversedSorting, setFilterColumn, setFilterData, setSortColumn, setReversedSorting}
     };
    
    let transfered_data : TableData<any> = {data: (data as SelectDTO<any>), filterColumn, filterData, sortColumn, reversedSorting, setFilterColumn, setFilterData, setSortColumn, setReversedSorting}
    
    useEffect(() => {
        loadData()    
        const interval = setInterval(loadData, 10000)
        return () => clearInterval(interval)
    }, [page, filterColumn, filterData, sortColumn, reversedSorting, modalOpen])

    function modalOnClose(){
        setModalOpen("")
        loadData()    
    }
    return (
        <>
        <BaseFrame isOpen={modalOpen != ""} onClose={modalOnClose} zindex={1000} width="40%">
            <>
                {modalOpen=="LocationCreate" && <CreateLocationSF onClose={modalOnClose}/>}
                {modalOpen=="CoordinateCreate" && <CreateCoordinateSF onClose={modalOnClose}/>}
                {modalOpen=="DisciplineCreate" && <CreateDisciplineSF onClose={modalOnClose}/>}
                {modalOpen=="PersonCreate" && <CreatePersonSF onClose={modalOnClose}/>}
                {modalOpen=="LabWorkCreate" && <CreateLabWorkSF onClose={modalOnClose}/>}
            </>
        </BaseFrame>
            <div className="modal-main-content">
              <div className="modal-header">Выбор {objectName}</div>
                <div className="modal-params-container">
                   <div className='table-container'>
                        <div className='table-controllers'>
                        <div className='menu-button-container table-previous-button'>
                            <button className={`menu-button ${page === 1 ? "table-button-inactive" : ""}`} onClick={()=>setPage(page-1)}>
                                Прошлая страница
                            </button>
                        </div>
                        <div className='menu-button-container table-next-button'>
                            <button className={`menu-button ${!nextPageExists ? "table-button-inactive" : ""}`} onClick={()=>{setPage(page+1)}}>
                            Следующая страница
                            </button>
                        </div>
                        </div>
                        {transfered_data !== null && data.success &&
                        <div className='table-body'>
                            {params.objectName == 'Location' && <LocationTable tableData={{...transfered_data, data: (data as SelectDTO<Location>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} />}
                            {params.objectName == 'Discipline' && <DisciplineTable tableData={{...transfered_data, data: (data as SelectDTO<Discipline>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} />}
                            {params.objectName == 'Coordinate' && <CoordinateTable tableData={{...transfered_data, data: (data as SelectDTO<Coordinate>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} />}
                            {params.objectName == 'Person' && <PersonTable tableData={{...transfered_data, data: (data as SelectDTO<Person>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} />}
                            {params.objectName == 'LabWork' && <LabWorkTable tableData={{...transfered_data, data: (data as SelectDTO<LabWork>)}} chosenRow={chosenRow} setChosenRow={setChosenRow} />}
                            <div className='main-table-add-button' onClick={() => setModalOpen(params.objectName+"Create")}>Добавить объект</div>
                        </div>
                        }
                    </div>
                </div>
            </div>
            <div className="modal-buttons">
                <button className="modal-close-button" onClick={onClose}>Закрыть</button>
                 <button className={`${required ? "modal-button-disbled" : ""} modal-save-button`} onClick={() =>{setChosenRow(-1); onClose()}}>
                    Очистить
                </button>
                <button className={`${(chosenRow == -1) ? "modal-button-disbled" : ""} modal-save-button`} onClick={onClose}>
                    Выбрать
                </button>
            </div>
        </>
    )
}

export default ChooseSF