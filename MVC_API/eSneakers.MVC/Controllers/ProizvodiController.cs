using ePhotographyEquipment.WEB.Models;
using eSneakers.DATA.Models;
using eSneakers.WEB.Data;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;

namespace eSneakers.MVC.Controllers
{
    public class ProizvodiController : Controller
    {
        MojContext mc = new MojContext();
        // GET: Proizvodi
        public ActionResult Pretraga(string q, int tipProizvoda)
        {

            List<ProizvodVM> model = mc.Proizvodi.Where(x => x.Naziv.Contains(q)).Select(x => new ProizvodVM {
                Id = x.Id,
                Akcija = x.Akcija,
                Cijena = x.Cijena,
                Naziv = x.Naziv,
                Ocjena = x.Ocjena,
                Opis=x.Opis,
                Popust=x.Popust,
                Sifra=x.Sifra,
                Slika=x.Slika,
                TipProizvodaId=x.TipProizvodaId



           }).ToList();
            if (tipProizvoda != 0)
            {
                model = model.Where(x => x.TipProizvodaId == tipProizvoda).ToList();
            }



            return Json(model, JsonRequestBehavior.AllowGet);
        }



        public ActionResult InsertImage(int id, string path)
        {

            byte[] buff = null;
            FileStream fs = new FileStream(path,
                                           FileMode.Open,
                                           FileAccess.Read);
            BinaryReader br = new BinaryReader(fs);
            long numBytes = new FileInfo(path).Length;
            buff = br.ReadBytes((int)numBytes);


            Proizvod p = mc.Proizvodi.Find(id);
            if (p != null)
            {


                p.Slika = buff;
                mc.SaveChanges();
            }

            return null;
        }





        public ActionResult Ocijeni(int id, float ocjena, int korisnik)
        {
            Ocjena o = mc.Ocjene.Where(x => x.ProizvodId == id && x.KorisnikId == korisnik).FirstOrDefault();
            if (o == null) {

                o = new Ocjena();
                mc.Ocjene.Add(o);
                o.KorisnikId = korisnik;
                o.ProizvodId = id;
                o.DatumKreiranja = DateTime.Now;
                o.OcjenaKorisnika = ocjena;
               
                
           

            try
            {
                mc.SaveChanges();
            }
            catch
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }



        
            List<Ocjena> ocjene = mc.Ocjene.Where(x => x.ProizvodId == id).ToList();

            float prosjecnaOcjena = ocjene.Average(x => x.OcjenaKorisnika);

            Proizvod p = mc.Proizvodi.Find(id);
            p.Ocjena = prosjecnaOcjena;
            try
            {
                mc.SaveChanges();
            }
            catch
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            return new HttpStatusCodeResult(HttpStatusCode.OK);
            }


            return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
        }
        }
    
}