import { useEffect, useState } from "react"
import ParameterField from "../FrameComponents/ParameterField"
import { processIsValidFloat, processIsLowerThan, processIsBiggerThan } from "../../../Utils/validations"
import ErrorPanel from "../FrameComponents/ErrorPanel"
import { makeQueryCreate, makeQueryDelete } from "../../../Utils/baseMaps"
import { createObject, removeObject } from "../../../Utils/apiQueries"
import type { Coordinate } from "../../../interfaces/Entities/Coordinate"
import BaseFrame from "../BaseFrame"
import DeleteAlert from "./DeleteAlert"
import ShowSF from "./ShowSF"

function CreateCoordinateSF({ onClose, id, coordinate }: { onClose: () => void, id: number, coordinate: Coordinate|undefined }) {
    const [x, setX] : [any, React.Dispatch<any>]= useState("")
    const [y, setY] : [any, React.Dispatch<any>] = useState("")
    const [errorMsgs, setErrorMsg] = useState<string[]>([])

    const [alert, setAlert] = useState(false)
    const [modalOpen, setModalOpen] = useState("")
    function showConnected(){
        setModalOpen("ShowLabWork")
    }
    function modalOnClose(){
        setModalOpen("")
    }
    useEffect(() => {
        if(coordinate != undefined){
            setX(coordinate.x?.toString())
            setY(coordinate.y?.toString()) 
        }
    },[])

    useEffect(() => {
        let res : string[] = []
        processIsValidFloat(x, false, "X", res)
        processIsLowerThan(x, 292, true, "X", res)
        processIsValidFloat(y, false, "Y", res)
        processIsBiggerThan(y, -244, true,"Y", res)
        setErrorMsg(res)
    }, [x, y]);

    function createQuery(){
        let form = new URLSearchParams()
        if(id != -1) form.append('id', id.toString())
        form.append('x', x)
        form.append('y', y)
        createObject(makeQueryCreate("Coordinate"),form)
        onClose()
    }

    function deleteQuery(){
        removeObject(makeQueryDelete("Coordinate", id.toString()))
        .then(() =>onClose())
        .catch(() => {
            setAlert(true)
        })
    }

    return (
        <>
            <BaseFrame isOpen={alert} onClose={() => setAlert(false)} zindex={5000} width="30%" height="35%">
                <>
                    <DeleteAlert onClose={() => setAlert(false)}></DeleteAlert>
                </>
            </BaseFrame>
            <BaseFrame isOpen={modalOpen != ""} onClose={modalOnClose} zindex={1000} width="40%" height="">
                <>
                    {modalOpen=="ShowLabWork" && <ShowSF onClose={modalOnClose} objectName="LabWork" filterColumnEx="coordinateId" filterDataEx={id.toString()}/>}
                </>
            </BaseFrame>
            <div className="modal-main-content">
              <div className="modal-header">{id == -1 ? "Создание" : "Изменение"} Coordinate</div>
                <div className="modal-params-container">
                    <ParameterField value={x} setValue={setX} required={true} type="floatNum" field="X" />
                    <ParameterField value={y} setValue={setY} required={true} type="floatNum" field="Y" />
                </div>
                <ErrorPanel errorMessages={errorMsgs} />  
            </div>
            <div className="modal-buttons">
                <button className="modal-close-button" onClick={onClose}>Закрыть</button>
                {
                    coordinate != undefined && 
                    <button className={`modal-save-button`} onClick={showConnected}>
                        Связанные
                    </button>
                }
                {
                    coordinate != undefined && 
                    <button className={`modal-save-button`} onClick={deleteQuery}>
                        Удалить
                    </button>
                }
                <button className={`${errorMsgs.length != 0 && "modal-button-disbled"} modal-save-button`} onClick={createQuery}>
                    {id == -1 ? "Создать" : "Изменить"}
                </button>
            </div>
        </>
    )
}

export default CreateCoordinateSF